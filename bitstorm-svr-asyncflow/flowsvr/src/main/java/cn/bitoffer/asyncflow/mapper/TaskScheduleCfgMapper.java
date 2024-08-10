package cn.bitoffer.asyncflow.mapper;

import cn.bitoffer.asyncflow.model.TaskScheduleCfgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskScheduleCfgMapper {
    TaskScheduleCfgModel getTaskTypeCfg(String taskType);


    void save(TaskScheduleCfgModel scheduleConfig);
    
    List<TaskScheduleCfgModel> getTaskTypeCfgList();
}
