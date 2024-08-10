package com.zdf.worker.core.observers;

import com.alibaba.fastjson.JSON;
import com.zdf.worker.Client.TaskFlower;
import com.zdf.worker.Client.TaskFlowerImpl;
import com.zdf.worker.boot.AppLaunch;
import com.zdf.worker.constant.UserConfig;
import com.zdf.worker.core.AnnType;
import com.zdf.worker.core.ObserverFunction;
import com.zdf.worker.data.*;
import com.zdf.worker.enums.TaskStatus;
import com.zdf.worker.task.TaskBuilder;
import com.zdf.worker.task.TaskRet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.zdf.worker.boot.AppLaunch.packageName;

/**
 * 观察者
 */
public class TimeObserver implements ObserverFunction{
    private Long beginTime;
    TaskFlower taskFlower = new TaskFlowerImpl();

    // 获取任务时改变任务状态
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onObtain)
    public void onObtain(List<AsyncTaskReturn> asyncTaskReturnList, List<AsyncTaskBase> asyncTaskBaseList) {
        System.out.println("开始加载上下文");
        convertModel(asyncTaskReturnList, asyncTaskBaseList);

    }

    public void convertModel(List<AsyncTaskReturn> asyncTaskReturnList, List<AsyncTaskBase> asyncTaskBaseList ) {
        for (AsyncTaskReturn asyncTaskReturn : asyncTaskReturnList) {
            AsyncTaskBase asyncTaskBase = new AsyncTaskBase();
            asyncTaskBase.setUser_id(asyncTaskReturn.getUser_id());
            asyncTaskBase.setTask_id(asyncTaskReturn.getTask_id());
            asyncTaskBase.setTask_type(asyncTaskReturn.getTask_type());
            asyncTaskBase.setTask_stage(asyncTaskReturn.getTask_stage());
            asyncTaskBase.setCrt_retry_num(asyncTaskReturn.getCrt_retry_num());
            asyncTaskBase.setMax_retry_num(asyncTaskReturn.getMax_retry_num());
            asyncTaskBase.setMax_retry_interval(asyncTaskReturn.getMax_retry_interval());
            asyncTaskBase.setCreate_time(asyncTaskReturn.getCreate_time());
            asyncTaskBase.setModify_time(asyncTaskReturn.getModify_time());
            asyncTaskBase.setSchedule_log(JSON.parseObject(String.valueOf(JSON.parse(asyncTaskReturn.getSchedule_log())), ScheduleLog.class));
            TaskRet<NftTaskContext> contextLoad = null;
            try {
                contextLoad = reflictMethod(getaClass(asyncTaskBase.getTask_type()), "contextLoad", new Object[] {asyncTaskReturn.getTask_context()}, new Class[]{String.class});
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(contextLoad)) {
                asyncTaskBase.setTask_context(contextLoad.getResult());
            }
            asyncTaskBase.setTask_context(JSON.parseObject(asyncTaskReturn.getTask_context(), NftTaskContext.class));
            asyncTaskBase.setTask_id(asyncTaskReturn.getTask_id());
            asyncTaskBase.setStatus(asyncTaskReturn.getStatus());
            asyncTaskBaseList.add(asyncTaskBase);
        }
    }

    public Class<?> getaClass(String taskType) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(packageName + "." + taskType);
        return aClass;
    }


    // 执行任务前做的动作，目前是简单打印
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onExecute)
    public void onExecute(AsyncTaskBase asyncTaskReturn) {
        this.beginTime = System.currentTimeMillis();
        System.out.println(asyncTaskReturn.getTask_type() + "开始执行。");
    }

    // 启动动作
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onBoot)
    public void onBoot() {
        System.out.println("--------------------------");
        System.out.println("控制台看到这个信息，证明你已经运行成功了~");
        System.out.println(UserConfig.USERID + "的线程" + Thread.currentThread().getName() + "取任务");
    }
    // 执行任务失败时的动作，目前是本地重试
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onError)
    public void onError(AsyncTaskBase asyncTaskReturn, ScheduleConfig scheduleConfig, List<AsyncTaskBase> asyncTaskBaseList, Class<?> aClass, Exception e) {
//        if (asyncTaskReturn.getCrt_retry_num() < 60) {
//            if (asyncTaskReturn.getCrt_retry_num() != 0) {
//                asyncTaskReturn.setMax_retry_num(asyncTaskReturn.getCrt_retry_num() << 1);
//            }
//        } else {
//            asyncTaskReturn.setMax_retry_interval(scheduleConfig.getRetry_interval());
//        }
//        if (asyncTaskReturn.getMax_retry_interval() > scheduleConfig.getRetry_interval()) {
//            asyncTaskReturn.setMax_retry_interval(scheduleConfig.getRetry_interval());
//        }
//        asyncTaskReturn.getSchedule_log().getLastData().setErrMsg(e.getMessage());
//        if (asyncTaskReturn.getMax_retry_num() == 0 || asyncTaskReturn.getCrt_retry_num() >= asyncTaskReturn.getMax_retry_num()) {
//            AsyncTaskSetRequest asyncTaskSetRequest = modifyStatus(asyncTaskReturn, TaskStatus.FAIL);
//            asyncTaskSetRequest.setCrt_retry_num(asyncTaskReturn.getCrt_retry_num());
//            asyncTaskSetRequest.setMax_retry_interval(asyncTaskReturn.getMax_retry_interval());
//            asyncTaskSetRequest.setMax_retry_num(asyncTaskReturn.getMax_retry_num());
//            setTaskNow(asyncTaskSetRequest);
//            return;
//        }
        System.out.println(asyncTaskReturn.getTask_type() + "任务执行出错！");
        e.printStackTrace();
        AsyncTaskSetRequest asyncTaskSetRequest;
        if (asyncTaskReturn.getMax_retry_num() == 0
                || asyncTaskReturn.getCrt_retry_num() >= asyncTaskReturn.getMax_retry_num()) {
            asyncTaskSetRequest = modifyTaskInfo(asyncTaskReturn, TaskStatus.FAIL, null);
            asyncTaskSetRequest.setSchedule_log(JSON.toJSONString(asyncTaskReturn.getSchedule_log()));
            asyncTaskSetRequest.setCrt_retry_num(asyncTaskReturn.getMax_retry_num());
            reflictMethod(aClass, "handleError", new Object[0], new Class[0]);
        } else {
            asyncTaskSetRequest = modifyTaskInfo(asyncTaskReturn, TaskStatus.PENDING, null);
            asyncTaskSetRequest.setSchedule_log(JSON.toJSONString(asyncTaskReturn.getSchedule_log()));
            asyncTaskSetRequest.setCrt_retry_num(asyncTaskReturn.getCrt_retry_num() + 1);

        }
        asyncTaskSetRequest.setOrder_time(System.currentTimeMillis() + (scheduleConfig.getRetry_interval() << asyncTaskReturn.getCrt_retry_num()));
        asyncTaskSetRequest.setMax_retry_interval(asyncTaskReturn.getMax_retry_interval());
        asyncTaskSetRequest.setMax_retry_num(asyncTaskReturn.getMax_retry_num());
        asyncTaskSetRequest.setSchedule_log(getScheduleLog(asyncTaskReturn, System.currentTimeMillis() - beginTime, e.getMessage()));
        setTaskNow(asyncTaskSetRequest);
    }
    // 任务执行完成做的动作
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onFinish)
    public void onFinish(AsyncTaskBase asyncTaskReturn, AsyncTaskSetStage asyncTaskSetStage, Class<?> aClass){
        AsyncTaskSetRequest asyncTaskSetRequest = modifyTaskInfo(asyncTaskReturn, TaskStatus.SUCCESS, asyncTaskSetStage);
        long cost = System.currentTimeMillis() - beginTime;
        asyncTaskSetRequest.setSchedule_log(JSON.toJSONString(getScheduleLog(asyncTaskReturn, cost, "")));
        System.out.println(asyncTaskReturn.getTask_type() + "执行完毕！");
        if (Objects.isNull(asyncTaskSetStage)) {
            reflictMethod(aClass, "handleFinish", new Object[0], new Class[0]);
        }
        setTaskNow(asyncTaskSetRequest);
    }

    private TaskRet reflictMethod(Class<?> aClass, String methodName, Object[] params, Class<?>[] paramsType) {
        TaskRet returnVal = null;
        if (Objects.nonNull(aClass)) {
            // 利用Java反射执行本地方法
            Method method = TaskBuilder.getMethod(aClass, methodName, params, paramsType);
            try {
                returnVal = (TaskRet) method.invoke(aClass.newInstance(), params);
                if (returnVal != null) {
                    Object result = returnVal.getResult();
                    System.out.println("执行结果为：" + result);
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return returnVal;
    }


    // 获取待定使用
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onStop)
    public void onStop(AsyncTaskBase asyncTaskReturn){
    }

    // 修改任务状态
    public AsyncTaskSetRequest modifyTaskInfo(AsyncTaskBase asyncTaskBase, TaskStatus taskStatus, AsyncTaskSetStage asyncTaskSetStage) {
        AsyncTaskSetRequest asyncTaskSetRequest = AsyncTaskSetRequest.builder().
                task_id(asyncTaskBase.getTask_id())
                        .task_context(asyncTaskSetStage != null ? JSON.toJSONString(asyncTaskSetStage.getTask_context()) : JSON.toJSONString(asyncTaskBase.getTask_context()))
                                .priority(asyncTaskBase.getPriority())
                                        .task_stage(asyncTaskSetStage != null ? asyncTaskSetStage.getTask_stage() : asyncTaskBase.getTask_stage()).status(taskStatus.getStatus())
                .crt_retry_num(asyncTaskBase.getCrt_retry_num())
                .max_retry_interval(asyncTaskBase.getMax_retry_interval())
                .order_time(asyncTaskSetStage != null ? System.currentTimeMillis() - asyncTaskBase.getPriority() : asyncTaskBase.getOrder_time() - asyncTaskBase.getPriority())
                .build();
        asyncTaskSetRequest.setStatus(asyncTaskSetStage != null ? TaskStatus.PENDING.getStatus() : taskStatus.getStatus());
        return asyncTaskSetRequest;
    }

    public String getScheduleLog(AsyncTaskBase asyncTaskReturn, long costTime, String errMsg) {
        // 记录调度信息
        ScheduleLog scheduleLog = asyncTaskReturn.getSchedule_log();
        ScheduleData lastData = scheduleLog.getLastData();
        List<ScheduleData> historyDatas = scheduleLog.getHistoryDatas();
        historyDatas.add(lastData);
        if (historyDatas.size() > 3) {
            historyDatas.remove(0);
        }
        ScheduleData scheduleData = new ScheduleData(UUID.randomUUID() + "", errMsg, costTime + "");
        scheduleLog.setLastData(scheduleData);
        return JSON.toJSONString(scheduleLog);
    }

    // 修改任务信息
    public void setTaskNow(AsyncTaskSetRequest asyncTaskSetRequest) {
        taskFlower.setTask(asyncTaskSetRequest);
    }
}
