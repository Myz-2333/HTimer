package com.zdf.worker.Client;

import com.zdf.worker.data.AsyncTaskRequest;
import com.zdf.worker.data.AsyncTaskReturn;
import com.zdf.worker.data.AsyncTaskSetRequest;
import com.zdf.worker.data.ScheduleConfig;
import com.zdf.worker.enums.TaskStatus;

import java.util.List;

public interface TaskFlower {
    public String createTask(AsyncTaskRequest asyncTaskRequest);
    public void setTask(AsyncTaskSetRequest asyncTaskSetRequest);
    public AsyncTaskReturn getTask(String taskId);
    public List<AsyncTaskReturn> getTaskList(Class<?> clazz, int status, int limit);
    public List<ScheduleConfig> getTaskTypeCfgList();
    public List<AsyncTaskReturn> getUserTaskList(List<TaskStatus> taskStatuses);
    public void createTaskCFG(ScheduleConfig scheduleConfig);


}
