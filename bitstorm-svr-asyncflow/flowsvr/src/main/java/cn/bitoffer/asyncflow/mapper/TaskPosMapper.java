package cn.bitoffer.asyncflow.mapper;

import cn.bitoffer.asyncflow.model.TaskPosModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskPosMapper {
    void create(TaskPosModel taskPos);

    void save(TaskPosModel taskPos);


    TaskPosModel getTaskPos(String taskType);

    List<TaskPosModel> getTaskPosList();
}
