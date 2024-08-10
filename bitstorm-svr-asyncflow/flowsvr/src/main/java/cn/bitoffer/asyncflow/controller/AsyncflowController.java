package cn.bitoffer.asyncflow.controller;

import cn.bitoffer.asyncflow.common.ResponseEntity;
import cn.bitoffer.asyncflow.common.ResponseEnum;
import cn.bitoffer.asyncflow.dto.*;
import cn.bitoffer.asyncflow.service.AsyncflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/v1")
@Slf4j
public class AsyncflowController {

    @Autowired
    private AsyncflowService asyncflowService;


    // v1
    @PostMapping("/create_task")
    public ResponseEntity<CreateReply> createTask(@RequestBody CreateTaskRequest request) {
        log.info("create task");
        if (request.taskData.getTaskType().isEmpty()) {
            log.error("任务类型为空");
            return ResponseEntity.fail(ResponseEnum.CREATE_TASK_FAIL);
        }

        String taskId = asyncflowService.createTask(request);


        return ResponseEntity.ok(new CreateReply(taskId));
    }

    @GetMapping("/get_task")
    public ResponseEntity<GetTaskReply> getTask(@RequestParam(value = "taskId") String taskId) {
        GetTaskRequest request = new GetTaskRequest();
        request.setTaskId(taskId);
        return ResponseEntity.ok(asyncflowService.getTask(request));
    }

    @PostMapping("/hold_tasks")
    public ResponseEntity<HoldTasksReply> holdTasks(@RequestBody HoldTasksRequest request) {

        if (request.getLimit() <= 0) {
            request.setLimit(DEFAULT_TASK_LIST_LIMIT);
        }

        if (request.getLimit() > MAX_TASK_LIST_LIMIT) {
            request.setLimit(MAX_TASK_LIST_LIMIT);
        }

        return ResponseEntity.ok(asyncflowService.holdTasks(request));
    }

    // 拉取任务列表接口
    @GetMapping("/get_task_list")
    public ResponseEntity<GetTaskListReply> getTaskList(@RequestParam(value = "taskType") String taskType,
                                                        @RequestParam(value = "limit", required = false) int limit,
                                                        @RequestParam(value = "status")  int status) {

        GetTaskListRequest request = new GetTaskListRequest();
        if (limit <= 0) {
            request.setLimit(DEFAULT_TASK_LIST_LIMIT);
        }

        if (limit > MAX_TASK_LIST_LIMIT) {
            request.setLimit(MAX_TASK_LIST_LIMIT);
        }

        request.setStatus(status);
        request.setTaskType(taskType);
        return ResponseEntity.ok(asyncflowService.getTaskList(request));
    }


    @GetMapping("/get_task_schedule_cfg_list")
    public ResponseEntity<GetTaskScheduleCfgListReply> getTaskScheduleCfgList(@RequestBody GetTaskScheduleCfgListRequest request) {

        return ResponseEntity.ok(asyncflowService.getTaskScheduleCfgList(request));
    }

    @PostMapping("/set_task")
    public ResponseEntity<SetTaskReply> setTask(@RequestBody SetTaskRequest request) {
        if (request.getTaskData().getTaskId().isEmpty()) {
            log.error("任务id为空");
            return ResponseEntity.fail(ResponseEnum.Set_Task_Fail);
        }
        asyncflowService.setTask(request);
        return ResponseEntity.ok();
    }


    // ping
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }


    private static final int MAX_PRIORITY = 3600 * 24 * 30 * 12;
    private static final int MAX_TASK_LIST_LIMIT = 1000;
    private static final int DEFAULT_TASK_LIST_LIMIT = 1000;
}
