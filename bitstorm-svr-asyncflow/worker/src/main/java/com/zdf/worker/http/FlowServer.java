package com.zdf.worker.http;

import com.zdf.worker.data.*;


public interface FlowServer {
    ReturnStatus getTaskList(String taskType, int status, int limit);
    ReturnStatus createTask(AsyncTaskRequest asyncTaskRequest);
    ReturnStatus setTask(AsyncTaskSetRequest asyncTaskSetRequest);
    ReturnStatus getTask(String taskId);

    ReturnStatus getTaskTypeCfgList();
    ReturnStatus getUserTaskList(String user_id, int statusList);
    ReturnStatus createTaskCFG(ScheduleConfig scheduleConfig);

}
