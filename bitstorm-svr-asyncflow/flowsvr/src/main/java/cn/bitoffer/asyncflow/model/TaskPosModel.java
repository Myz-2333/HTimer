package cn.bitoffer.asyncflow.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskPosModel implements Serializable {
    public Long id;
    public String taskType;
    public Integer scheduleBeginPos;
    public Integer scheduleEndPos;
    public LocalDateTime createTime;
    public LocalDateTime modifyTime;

    @Override
    public String toString() {
        return "TaskPosModel{" +
                "id=" + id +
                ", taskType='" + taskType + '\'' +
                ", scheduleBeginPos=" + scheduleBeginPos +
                ", scheduleEndPos=" + scheduleEndPos +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
