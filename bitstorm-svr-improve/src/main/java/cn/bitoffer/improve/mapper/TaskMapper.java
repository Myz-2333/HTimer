package cn.bitoffer.improve.mapper;


import cn.bitoffer.improve.model.Count;
import cn.bitoffer.improve.model.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface TaskMapper {

    /**
     * 保存Task
     *
     * @param task
     */
    void save(@Param("task") Task task);

    /**
     * 根据CountId查询Count
     *
     * @return Count
     */
    Task getTask(@Param("taskID") String taskID);

}