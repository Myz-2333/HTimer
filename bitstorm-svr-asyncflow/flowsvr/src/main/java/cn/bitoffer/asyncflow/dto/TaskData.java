package cn.bitoffer.asyncflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskData {
    private int status;
    private Long priority;
    private Long crtRetryNum;
    private Long maxRetryNum;
    private Long maxRetryInterval;
    private String userId;
    private String taskId;
    private String taskType;
    private String Stage;
    private String context;

    private String scheduleLog;
    private Long orderTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateTimeSerializer.class)		// 序列化
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateTimeSerializer.class)		// 序列化
    private LocalDateTime modifyTime;
}