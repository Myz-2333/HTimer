package cn.bitoffer.asyncflow.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateReply implements Serializable {
    public String taskId;

    public CreateReply(String taskId) {
        this.taskId = taskId;
    }

}
