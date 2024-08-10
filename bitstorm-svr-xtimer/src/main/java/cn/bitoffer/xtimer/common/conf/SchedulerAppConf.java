package cn.bitoffer.xtimer.common.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SchedulerAppConf {

    @Value("${scheduler.bucketsNum}")
    private int bucketsNum;
    @Value("${scheduler.tryLockSeconds}")
    private int tryLockSeconds;
    @Value("${scheduler.tryLockGapMilliSeconds}")
    private int tryLockGapMilliSeconds;
    @Value("${scheduler.successExpireSeconds}")
    private int successExpireSeconds;

    @Value("${scheduler.pool.corePoolSize}")
    private int corePoolSize;

    @Value("${scheduler.pool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${scheduler.pool.queueCapacity}")
    private int queueCapacity;

    @Value("${scheduler.pool.namePrefix}")
    private String namePrefix;

    public int getBucketsNum() {
        return bucketsNum;
    }

    public void setBucketsNum(int bucketsNum) {
        this.bucketsNum = bucketsNum;
    }

    public int getTryLockSeconds() {
        return tryLockSeconds;
    }

    public void setTryLockSeconds(int tryLockSeconds) {
        this.tryLockSeconds = tryLockSeconds;
    }

    public int getTryLockGapMilliSeconds() {
        return tryLockGapMilliSeconds;
    }

    public void setTryLockGapMilliSeconds(int tryLockGapMilliSeconds) {
        this.tryLockGapMilliSeconds = tryLockGapMilliSeconds;
    }

    public int getSuccessExpireSeconds() {
        return successExpireSeconds;
    }

    public void setSuccessExpireSeconds(int successExpireSeconds) {
        this.successExpireSeconds = successExpireSeconds;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
}