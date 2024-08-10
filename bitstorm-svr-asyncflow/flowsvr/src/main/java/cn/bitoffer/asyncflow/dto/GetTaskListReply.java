package cn.bitoffer.asyncflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetTaskListReply {
    public List<TaskData> taskList;
}


