package cn.bitoffer.asyncflow.service.impl;

import cn.bitoffer.asyncflow.dto.*;
import cn.bitoffer.asyncflow.enums.TaskStatus;
import cn.bitoffer.asyncflow.mapper.TaskMapper;
import cn.bitoffer.asyncflow.mapper.TaskPosMapper;
import cn.bitoffer.asyncflow.mapper.TaskScheduleCfgMapper;
import cn.bitoffer.asyncflow.model.TaskModel;
import cn.bitoffer.asyncflow.model.TaskPosModel;
import cn.bitoffer.asyncflow.model.TaskScheduleCfgModel;
import cn.bitoffer.asyncflow.redis.RedisUtil;
import cn.bitoffer.asyncflow.service.AsyncflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AsyncflowServiceImpl implements AsyncflowService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskPosMapper taskPosMapper;
    @Autowired
    private TaskScheduleCfgMapper taskScheduleCfgMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String createTask(CreateTaskRequest request) {
        TaskPosModel taskPos = taskPosMapper.getTaskPos(request.taskData.getTaskType());
        // 获取配置
        TaskScheduleCfgModel taskCfg = taskScheduleCfgMapper.getTaskTypeCfg(request.taskData.getTaskType());
        request.taskData.setMaxRetryNum(taskCfg.maxRetryNum);
        request.taskData.setMaxRetryInterval(taskCfg.maxRetryInterval);
        request.taskData.setOrderTime(System.currentTimeMillis() / 1000);
        TaskModel task = new TaskModel();
        fillTaskModel(request.taskData, task, taskPos.scheduleEndPos);

        // 写入DB
        taskMapper.create(getTableName(task.getTaskType(), taskPos.scheduleEndPos.toString()), task);
        return task.getTaskId();
    }

    @Override
    public GetTaskListReply getTaskList(GetTaskListRequest request) {
        String taskType = request.getTaskType();
        int limit = request.getLimit();
        int status = request.getStatus();

        GetTaskListReply reply = new GetTaskListReply();
        TaskPosModel taskPos = taskPosMapper.getTaskPos(taskType);
        String tableName = getTableName(taskType, taskPos.scheduleBeginPos.toString());

        List<TaskModel> taskModelList = taskMapper.getTaskList(tableName, status, limit);

        List<TaskData> tasks = new ArrayList<>();
        for (TaskModel taskModel : taskModelList) {
            TaskData task = new TaskData();
            fillTaskResp(task, taskModel);
            tasks.add(task);
        }
        // 放入reply中，返回结果
        reply.setTaskList(tasks);
        return reply;
    }

    @Override
    public GetTaskScheduleCfgListReply getTaskScheduleCfgList(GetTaskScheduleCfgListRequest request) {
        List<TaskScheduleCfgModel> list = taskScheduleCfgMapper.getTaskTypeCfgList();
        GetTaskScheduleCfgListReply reply = new GetTaskScheduleCfgListReply();
        reply.setTaskScheduleCfgList(list);
        return reply;
    }


    @Override
    public GetTaskReply getTask(GetTaskRequest request) {

        String taskId = request.getTaskId();
        List<String> list = getTablePosFromTaskId(taskId);
        String taskType = list.get(0), pos = list.get(1);
        String tableName = getTableName(taskType, pos);

        TaskModel task = taskMapper.getTask(tableName, taskId);
        GetTaskReply reply = new GetTaskReply();
        fillTaskResp(reply.getTaskData(), task);
        return reply;
    }

    @Override
    public SetTaskReply setTask(SetTaskRequest request) {
        String taskId = request.getTaskData().getTaskId();
        List<String> list = getTablePosFromTaskId(taskId);
        String taskType = list.get(0), pos = list.get(1);
        String tableName = getTableName(taskType, pos);

        TaskModel task = new TaskModel();
        fillTaskModel(request.getTaskData(), task, Long.parseLong(pos));
        taskMapper.save(tableName, task);
        return new SetTaskReply();
    }

    @Override
    public HoldTasksReply holdTasks(HoldTasksRequest request) {
        String taskType = request.getTaskType();
        int limit = request.getLimit();

        HoldTasksReply reply = new HoldTasksReply();
        TaskPosModel taskPos = taskPosMapper.getTaskPos(taskType);
        String tableName = getTableName(taskType, taskPos.scheduleBeginPos.toString());

        List<TaskModel> taskModelList = taskMapper.getTaskList(tableName, TaskStatus.TASK_STATUS_PENDING.getStatus(), limit);

        List<TaskData> tasks = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        for (TaskModel taskModel : taskModelList) {
            // 如果有重试次数，并且重试间隔未过，则过滤掉
            if (taskModel.getCrtRetryNum() != 0 && taskModel.getMaxRetryInterval() != 0 && taskModel.getOrderTime() > System.currentTimeMillis() / 1000) {

                continue;
            }

            TaskData task = new TaskData();
            fillTaskResp(task, taskModel);
            tasks.add(task);
            idList.add(taskModel.getTaskId());
        }

        System.out.println("idList: " + idList);
        // 更新任务状态
        if (!idList.isEmpty()) {
            taskMapper.updateStatusBatch(idList, TaskStatus.TASK_STATUS_PROCESSING.getStatus(), tableName);

        }

        // 放入reply中，返回结果
        reply.setTaskList(tasks);
        return reply;
    }

    // genTaskId 生成对应taskId
    private String genTaskId(String taskType, String pos) {
        taskType = taskType.replace("_", "-");
        return String.format("%s_%s_%s", UUID.randomUUID(), taskType, pos);
    }

    private void fillTaskModel(TaskData task, TaskModel dbTask, long pos) {
        dbTask.setUserId(task.getUserId());
        if (task.getTaskId().isEmpty()) {
            dbTask.setTaskId(genTaskId(task.getTaskType(), Long.toString(pos)));
            dbTask.setStatus(TaskStatus.TASK_STATUS_PENDING.getStatus());
        } else {
            dbTask.setTaskId(task.getTaskId());
            dbTask.setStatus(task.getStatus());
        }
        dbTask.setTaskType(task.getTaskType());
        dbTask.setUserId(task.getUserId());
        dbTask.setScheduleLog(task.getScheduleLog());
        dbTask.setTaskStage(task.getStage());
        dbTask.setCrtRetryNum(task.getCrtRetryNum());
        dbTask.setMaxRetryNum(task.getMaxRetryNum());
        dbTask.setMaxRetryInterval(task.getMaxRetryInterval());
        dbTask.setTaskContext(task.getContext());
    }

    private void fillTaskResp(TaskData task, TaskModel dbTask) {
        task.setUserId(dbTask.getUserId());
        task.setTaskId(dbTask.getTaskId());
        task.setTaskType(dbTask.getTaskType());
        task.setStatus(dbTask.getStatus());
        task.setScheduleLog(dbTask.getScheduleLog());
        task.setPriority(dbTask.getPriority());
        task.setStage(dbTask.getTaskStage());
        task.setCrtRetryNum(dbTask.getCrtRetryNum());
        task.setMaxRetryNum(dbTask.getMaxRetryNum());
        task.setMaxRetryInterval(dbTask.getMaxRetryInterval());
        task.setContext(dbTask.getTaskContext());
        task.setCreateTime(dbTask.getCreateTime());
        task.setModifyTime(dbTask.getModifyTime());
    }


    private String getTableName(String taskType, String pos) {
        return String.format("t_%s_%s_%s", taskType, "task", pos);
    }

    private List<String> getTablePosFromTaskId(String taskId) {
        List<String> list = new ArrayList<String>();
        String[] s = taskId.split("_");
        if (s.length == 3) {
            s[1] = s[1].replace("-", "_");
            // 任务类型
            list.add(s[1]);
            // pos
            list.add(s[2]);
        } else {
            list.add("");
            log.error("taskId {} have not match _", taskId);
        }

        return list;
    }
}