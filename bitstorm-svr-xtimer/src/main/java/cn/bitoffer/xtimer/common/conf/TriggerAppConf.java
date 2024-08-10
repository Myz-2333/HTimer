package cn.bitoffer.xtimer.common.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TriggerAppConf {
    @Value("${trigger.zrangeGapSeconds}")
    private int zrangeGapSeconds;
    @Value("${trigger.workersNum}")
    private int workersNum;

    @Value("${trigger.pool.corePoolSize}")
    private int corePoolSize;

    @Value("${trigger.pool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${trigger.pool.queueCapacity}")
    private int queueCapacity;

    @Value("${trigger.pool.namePrefix}")
    private String namePrefix;

    public int getZrangeGapSeconds() {
        return zrangeGapSeconds;
    }

    public void setZrangeGapSeconds(int zrangeGapSeconds) {
        this.zrangeGapSeconds = zrangeGapSeconds;
    }

    public int getWorkersNum() {
        return workersNum;
    }

    public void setWorkersNum(int workersNum) {
        this.workersNum = workersNum;
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