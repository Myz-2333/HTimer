package cn.bitoffer.xtimer.service.trigger;

import cn.bitoffer.xtimer.exception.ErrorCode;
import cn.bitoffer.xtimer.common.conf.TriggerAppConf;
import cn.bitoffer.xtimer.enums.TaskStatus;
import cn.bitoffer.xtimer.exception.BusinessException;
import cn.bitoffer.xtimer.mapper.TaskMapper;
import cn.bitoffer.xtimer.model.TaskModel;
import cn.bitoffer.xtimer.redis.TaskCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class TriggerTimerTask extends TimerTask {

    TriggerAppConf triggerAppConf;

    TriggerPoolTask triggerPoolTask;

    TaskCache taskCache;

    TaskMapper taskMapper;

    private CountDownLatch latch ;
    private Long count = 0L;

    private Date startTime;

    private Date endTime;

    private String minuteBucketKey;

    public TriggerTimerTask(TriggerAppConf triggerAppConf,TriggerPoolTask triggerPoolTask,
                            TaskCache taskCache,TaskMapper taskMapper,CountDownLatch latch,
                            Date startTime, Date endTime, String minuteBucketKey) {
        this.triggerAppConf = triggerAppConf;
        this.triggerPoolTask = triggerPoolTask;
        this.taskCache = taskCache;
        this.taskMapper = taskMapper;
        this.latch = latch;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minuteBucketKey = minuteBucketKey;
    }

    @Override
    public void run() {
        Date tStart = new Date(startTime.getTime() + count*triggerAppConf.getZrangeGapSeconds()*1000L);
        if(tStart.compareTo(endTime) > 0){
            latch.countDown();
            return;
        }
        // 处理任务
        try{
            handleBatch(tStart, new Date(tStart.getTime() + triggerAppConf.getZrangeGapSeconds()*1000L));
        }catch (Exception e){
            log.error("handleBatch Error. minuteBucketKey"+minuteBucketKey+",tStartTime:"+startTime+",e:",e);
        }
        count++;
    }

    private void handleBatch(Date start, Date end){
        //
        List<TaskModel> tasks = getTasksByTime(start,end);
        if (CollectionUtils.isEmpty(tasks)){
            return;
        }
        for (TaskModel task :tasks) {
            try {
                if(task == null){
                    continue;
                }
                triggerPoolTask.runExecutor(task);
            }catch (Exception e){
                log.error("executor run task error,task"+task.toString());
            }
        }
    }

    private List<TaskModel> getTasksByTime(Date start, Date end){
        List<TaskModel> tasks = new ArrayList<>();

        // 先走缓存
        try{
            tasks= taskCache.getTasksFromCache(minuteBucketKey,start.getTime(),end.getTime());
        }catch (Exception e){
            log.error("getTasksFromCache error: " ,e);
            // 缓存miss,走数据库
            try{
                tasks = taskMapper.getTasksByTimeRange(start.getTime(),end.getTime()-1, TaskStatus.NotRun.getStatus());
            }catch (Exception e1){
                log.error("getTasksByConditions error: " ,e1);
            }
        }
        return tasks;
    }
}
