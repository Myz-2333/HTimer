package cn.bitoffer.xtimer.service.trigger;

import cn.bitoffer.xtimer.common.conf.TriggerAppConf;
import cn.bitoffer.xtimer.exception.BusinessException;
import cn.bitoffer.xtimer.mapper.TaskMapper;
import cn.bitoffer.xtimer.redis.TaskCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class TriggerWorker {

    @Autowired
    TriggerAppConf triggerAppConf;

    @Autowired
    TriggerPoolTask triggerPoolTask;

    @Autowired
    TaskCache taskCache;

    @Autowired
    TaskMapper taskMapper;

    public void work(String minuteBucketKey){
        // 进行为时一分钟的zrange
        Date startTime = getStartMinute(minuteBucketKey);
        Date endTime = new Date(startTime.getTime() + 60000);

        CountDownLatch latch = new CountDownLatch(1);
        Timer timer = new Timer("Timer");
        TriggerTimerTask task = new TriggerTimerTask(
                triggerAppConf,triggerPoolTask,taskCache,taskMapper,latch,startTime,endTime,minuteBucketKey);
        timer.scheduleAtFixedRate(task, 0L, triggerAppConf.getZrangeGapSeconds()*1000L);
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("执行TriggerTimerTask异常中断，task:"+task);
        }finally{
            timer.cancel();
        }
    }

    private Date getStartMinute(String minuteBucketKey){
        String[] timeBucket = minuteBucketKey.split("_");
        if(timeBucket.length != 2){
            log.error("TriggerWorker getStartMinute 错误");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startMinute = null;
        try {
            startMinute = sdf.parse(timeBucket[0]);
        } catch (ParseException e) {
            log.error("TriggerWorker getStartMinute 错误");
        }
        return startMinute;
    }
}
