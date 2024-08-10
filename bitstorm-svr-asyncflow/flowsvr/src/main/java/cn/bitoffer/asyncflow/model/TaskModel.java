package cn.bitoffer.asyncflow.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskModel implements Serializable {
    private int id;
    private String userId;
    private String taskId;
    private String taskType;
    private String taskStage;
    private int status;
    private Long priority;
    private Long crtRetryNum;
    private Long maxRetryNum;
    private Long maxRetryInterval;
    private String scheduleLog;
    private String taskContext;
    private Long orderTime;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskStage='" + taskStage + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", crtRetryNum=" + crtRetryNum +
                ", maxRetryNum=" + maxRetryNum +
                ", maxRetryInterval=" + maxRetryInterval +
                ", scheduleLog='" + scheduleLog + '\'' +
                ", taskContext='" + taskContext + '\'' +
                ", orderTime=" + orderTime +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}

