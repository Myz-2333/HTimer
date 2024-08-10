package cn.bitoffer.asyncflow.mapper;

import cn.bitoffer.asyncflow.model.TaskModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {

    void create(@Param("tableName") String tableName, @Param("task") TaskModel task);

    void save(@Param("tableName") String tableName, @Param("task") TaskModel task);

    TaskModel getTask(@Param("tableName") String tableName, @Param("taskId") String taskId);

    List<TaskModel> getTaskList(@Param("tableName") String tableName, @Param("status") int status, @Param("limit") int limit);

    int getTaskCountByStatus(@Param("status") int status, @Param("tableName") String tableName);

    int getTaskCount(@Param("statusList") List<Integer> statusList, @Param("tableName") String tableName);

    void increaseCrtRetryNum(@Param("taskId") String taskId, @Param("tableName") String tableName);

    void updateStatusBatch(@Param("taskIdList") List<String> taskIdList, @Param("status") int status, @Param("tableName") String tableName);

}
