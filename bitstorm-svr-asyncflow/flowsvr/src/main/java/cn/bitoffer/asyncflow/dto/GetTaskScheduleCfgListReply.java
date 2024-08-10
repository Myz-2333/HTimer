package cn.bitoffer.asyncflow.dto;

import cn.bitoffer.asyncflow.model.TaskScheduleCfgModel;
import lombok.Data;

import java.util.List;

@Data
public class GetTaskScheduleCfgListReply {
    private List<TaskScheduleCfgModel> taskScheduleCfgList;

}
