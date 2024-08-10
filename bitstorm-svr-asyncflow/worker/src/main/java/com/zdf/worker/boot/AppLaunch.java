package com.zdf.worker.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zdf.worker.Client.TaskFlower;
import com.zdf.worker.Client.TaskFlowerImpl;
import com.zdf.worker.constant.TaskConstant;
import com.zdf.worker.constant.UserConfig;
import com.zdf.worker.core.ObserverManager;
import com.zdf.worker.core.observers.TimeObserver;
import com.zdf.worker.data.AsyncTaskBase;
import com.zdf.worker.data.AsyncTaskReturn;
import com.zdf.worker.data.AsyncTaskSetStage;
import com.zdf.worker.data.ScheduleConfig;
import com.zdf.worker.enums.TaskStatus;
import com.zdf.worker.task.Lark;
import com.zdf.worker.task.TaskBuilder;
import com.zdf.worker.task.TaskRet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AppLaunch implements Launch{
    final TaskFlower taskFlower;//用于发送请求

    public static String packageName; //要执行的类的包名

    // 拉取哪几类任务
    static Class taskType;

    // 拉取哪个任务的指针
    AtomicInteger offset;

    // 观察者模式的观察管理者
    ObserverManager observerManager;
    private Long intervalTime;//请求间隔时间，读取用户配置
    private int scheduleLimit; //一次拉取多少个任务，用户配置
    public Long cycleScheduleConfigTime = 10000L;// 多长时间拉取一次任务配置信息
    public static int MaxConcurrentRunTimes = 5; // 线程池最大数量
    public static int concurrentRunTimes = MaxConcurrentRunTimes; // 线程并发数
    private static String LOCK_KEY = "lock"; // 分布式锁的键
    Map<String, ScheduleConfig> scheduleCfgDic; // 存储任务配置信息
    Logger logger = LoggerFactory.getLogger(AppLaunch.class); //打印日志
    ThreadPoolExecutor threadPoolExecutor; // 拉取任务的线程池
    ScheduledExecutorService loadPool;


    public AppLaunch() {
        this(0);
    }
    public AppLaunch(int scheduleLimit) {
        scheduleCfgDic = new ConcurrentHashMap<>();

        loadPool = Executors.newScheduledThreadPool(1);
        taskFlower = new TaskFlowerImpl();
        taskType = Lark.class;
        packageName = taskType.getPackage().getName();
        this.scheduleLimit = scheduleLimit;
        observerManager = new ObserverManager();
        // 向观察管理者注册观察者
        observerManager.registerEventObserver(new TimeObserver());
        offset = new AtomicInteger(0);
        // 初始化，拉取任务配置信息
        init();

    }

    // 启动：拉取任务
    @Override
    public int start() {
        // 读取对应任务配置信息
        ScheduleConfig scheduleConfig = scheduleCfgDic.get(taskType.getSimpleName());
        // 如果用户没有配置时间间隔就使用默认时间间隔
        intervalTime = scheduleConfig.getSchedule_interval() == 0 ? TaskConstant.DEFAULT_TIME_INTERVAL * 1000L : scheduleConfig.getSchedule_interval() * 1000L;
        this.threadPoolExecutor = new ThreadPoolExecutor(concurrentRunTimes, MaxConcurrentRunTimes, intervalTime + 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(UserConfig.QUEUE_SIZE));
        for(;;) {
            if (UserConfig.QUEUE_SIZE - threadPoolExecutor.getQueue().size() >= scheduleLimit) {
                execute(taskType);
            }
            try {
                Thread.sleep(intervalTime + (int)(Math.random() * 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
//        for (int i = 0; i < concurrentRunTimes; i++) {
//            // 前后波动500ms
//            int step = (int) (Math.random() * 500 + 1);
//            // 拉取任务
//            threadPoolExecutor.scheduleAtFixedRate(this::execute, step * 3L, intervalTime + step, TimeUnit.MILLISECONDS);
//        }
    }

    public void execute(Class<?> taskType) {
        List<AsyncTaskBase> asyncTaskBaseList = scheduleTask(taskType);
        if (asyncTaskBaseList == null) {
            return;
        }
        int size = asyncTaskBaseList.size();
        for (int i = 0; i < size; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> executeTask(asyncTaskBaseList, finalI));
        }
    }

    // 拉取任务
    private List<AsyncTaskBase> scheduleTask(Class<?> taskType) {
        try {
            // 开始执行时，做点事，这里就是简单的打印了一句话，供后续扩展使用
            observerManager.wakeupObserver(ObserverType.onBoot);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // 调用拉取任务接口拉取任务
        List<AsyncTaskBase> asyncTaskBaseList = getAsyncTaskBases(observerManager, taskType);
        // 为空判断
        if (asyncTaskBaseList == null || asyncTaskBaseList.size() == 0) {
            return null;
        }
        return asyncTaskBaseList;
    }

    // 执行任务
    private void executeTask(List<AsyncTaskBase> asyncTaskBaseList, int i) {
        AsyncTaskBase v = asyncTaskBaseList.get(i);
        try {
            // 执行前干点事，这里就打印了一句话，后续可以扩展
            observerManager.wakeupObserver(ObserverType.onExecute, v);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        AsyncTaskSetStage asyncTaskSetStage = null;
        Class<?> aClass = null;
        try {
            // 利用Java反射执行本地方法
            aClass = getaClass(v.getTask_type());
            Method method = TaskBuilder.getMethod(aClass, v.getTask_stage(), v.getTask_context().getParams(), v.getTask_context().getClazz());
            System.out.println(method.getName());
            TaskRet returnVal = (TaskRet) method.invoke(aClass.newInstance(), v.getTask_context().getParams());
            if (returnVal != null) {
                asyncTaskSetStage = returnVal.getAsyncTaskSetStage();
                Object result = returnVal.getResult();
                System.out.println("执行结果为：" + result);
            }
        } catch (Exception e) {
            try {
                // 执行出现异常了（任务执行失败了）更改任务状态为PENDING，重试次数+1，超过重试次数设置为FAIL
                observerManager.wakeupObserver(ObserverType.onError, v, scheduleCfgDic.get(v.getTask_type()), asyncTaskBaseList, aClass, e);
                return;
            } catch (InvocationTargetException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        try {
            // 正常执行成功了干点事，方便后续扩展
            observerManager.wakeupObserver(ObserverType.onFinish, v, asyncTaskSetStage, aClass);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Class<?> getaClass(String taskType) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(packageName + "." + taskType);
        return aClass;
    }

    private List<AsyncTaskBase> getAsyncTaskBases(ObserverManager observerManager, Class<?> taskType) {
// 分布式锁的参数
//                LockParam lockParam = new LockParam(LOCK_KEY);
//         分布式锁
//        RedisLock redisLock = new RedisLock(lockParam);
        List<AsyncTaskReturn> taskList = null;
        try {
            // 上锁
         //   if (redisLock.lock()) {
            // 调用http请求接口
                taskList = taskFlower.getTaskList(taskType, TaskStatus.PENDING.getStatus(), scheduleCfgDic.get(taskType.getSimpleName()).getSchedule_limit());
                if (taskList == null || taskList.size() == 0) {
                    logger.warn("no task to deal!");
                    return null;
                }
                try {
                    List<AsyncTaskBase> asyncTaskBaseList = new ArrayList<>();
                    observerManager.wakeupObserver(ObserverType.onObtain, taskList, asyncTaskBaseList);
                    return asyncTaskBaseList;
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
       //     }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
        // 释放锁
//            redisLock.unlock();
//        }

        return null;
    }

    // 拉取任务配置信息
    private void loadCfg() {
        List<ScheduleConfig> taskTypeCfgList = taskFlower.getTaskTypeCfgList();
        for (ScheduleConfig scheduleConfig : taskTypeCfgList) {
            scheduleCfgDic.put(scheduleConfig.getTask_type(), scheduleConfig);
        }
    }

    @Override
    public int init() {
        loadCfg();
        if (scheduleLimit != 0) {
            logger.debug("init ScheduleLimit : %d", scheduleLimit);
            concurrentRunTimes = scheduleLimit;
            MaxConcurrentRunTimes = scheduleLimit;
        } else {
            this.scheduleLimit = this.scheduleCfgDic.get(taskType.getSimpleName()).getSchedule_limit();
        }
        // 定期更新任务配置信息

        loadPool.scheduleAtFixedRate(this::loadCfg, cycleScheduleConfigTime, cycleScheduleConfigTime, TimeUnit.MILLISECONDS);
        return 0;
    }




    @Override
    public int destroy() {
        return 0;
    }
    // 枚举
    public enum ObserverType {
        onBoot(0),
        onError(1),
        onExecute(2),
        onFinish(3),
        onStop(4), onObtain(5);
        private int code;

        private ObserverType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
