package cn.bitoffer.asyncflow.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskScheduleCfgModel implements Serializable {
    public String taskType;
    public long scheduleLimit;
    public long scheduleInterval;
    public long maxProcessingTime;
    public long maxRetryNum;
    public long retryInterval;
    public long maxRetryInterval;
    public LocalDateTime createTime;
    public LocalDateTime modifyTime;

    @Override
    public String toString() {
        return "TaskScheduleCfgModel{" +
                "taskType='" + taskType + '\'' +
                ", scheduleLimit=" + scheduleLimit +
                ", scheduleInterval=" + scheduleInterval +
                ", maxProcessingTime=" + maxProcessingTime +
                ", maxRetryNum=" + maxRetryNum +
                ", retryInterval=" + retryInterval +
                ", maxRetryInterval=" + maxRetryInterval +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}