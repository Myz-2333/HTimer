package cn.bitoffer.lottery.scheduler;

import cn.bitoffer.lottery.constant.Constants;
import cn.bitoffer.lottery.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Component
public class ShedulerUserTask {
    @Autowired
    private RedisUtil redisUtil;
    @Scheduled(cron = "0 0 0 * * ?")
    @PostConstruct
    public void scheduledTask() {
        log.info("重置今日用户抽奖次数：{}", LocalDateTime.now());
        for(int i = 0; i< Constants.userFrameSize; i++){
            String key = String.format(Constants.userLotteryDayNumPrefix+"%d", i);
            //  清理ip抽奖次数
            redisUtil.del(key);
        }
    }
}
