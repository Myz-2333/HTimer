package cn.bitoffer.improve.controller;

import cn.bitoffer.common.model.ResponseEntity;
import cn.bitoffer.improve.model.Task;
import cn.bitoffer.improve.redis.RedisExample;
import cn.bitoffer.improve.service.CountService;
import cn.bitoffer.improve.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * web服务接口：http 接口
 **/

@RestController
@RequestMapping("/demo")
@Slf4j
public class ImproveController {

    @Autowired
    private CountService countService;

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/incr_count", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> incrCount(@RequestBody IncrCountReq data) {
         countService.incrCount(data.num);
        return ResponseEntity.ok();
    }
    @PostMapping(value = "/incr_count_async", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> incrCountAsync(@RequestBody IncrCountReq data) {
        countService.incrCountAsync(data.num);
        return ResponseEntity.ok();
    }


    @PostMapping(value = "/create_task", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> CreateTask(@RequestBody CreateTaskReq data) {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        taskService.save(task);
        return ResponseEntity.ok();
    }

    public static class IncrCountReq {
        public Integer num;
    }

    public static class CreateTaskReq {
    }
}
