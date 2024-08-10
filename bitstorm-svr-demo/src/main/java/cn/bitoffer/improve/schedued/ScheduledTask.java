package cn.bitoffer.improve.schedued;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务模版
 *
 **/
@Component
public class ScheduledTask {

    @Scheduled(fixedRate = 3000)
    public void scheduledTask() {
        System.out.println("任务执行时间 is ：" + LocalDateTime.now());
    }

}
