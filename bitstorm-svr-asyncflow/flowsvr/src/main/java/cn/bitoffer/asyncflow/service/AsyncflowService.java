package cn.bitoffer.asyncflow.service;

import cn.bitoffer.asyncflow.dto.*;

/**
 * @author 狂飙训练营
 */
public interface AsyncflowService {

    String createTask(CreateTaskRequest request);

    GetTaskListReply getTaskList(GetTaskListRequest request);

    GetTaskScheduleCfgListReply getTaskScheduleCfgList(GetTaskScheduleCfgListRequest request);


    SetTaskReply setTask(SetTaskRequest request);

    //    /**
    //     * 获取任务
    //     *
    //     * @param task_id
    //     * @param <T>
    //     * @return
    //     */
    GetTaskReply getTask(GetTaskRequest request);


    //    <T> ReturnStatus<T> getTaskByUserIdAndStatus(String user_id, int statusList);


    HoldTasksReply holdTasks(HoldTasksRequest request);
}
