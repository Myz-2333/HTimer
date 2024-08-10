package cn.bitoffer.improve.controller;

import cn.bitoffer.common.model.ResponseEntity;
import cn.bitoffer.improve.redis.RedisExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * web服务接口：http 接口
 **/

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RedisExample redisExample;

    @GetMapping("/hello")
    public ResponseEntity<String> helloworld() {
        // 消息队列发送消息
        return ResponseEntity.ok(
                "hello world"
        );
    }

    @GetMapping("/kafkaDemo")
    public ResponseEntity<String> kafkaTest() {
        // 消息队列发送消息
        kafkaTemplate.send("hello-world","来自Kafka的测试消息【ABAB】");
        return ResponseEntity.ok(
                "发送消息成功"
        );
    }

    @GetMapping("/redisLuaDemo")
    public ResponseEntity<String> redisTest() {
        // 消息队列发送消息
        redisExample.luaExample();
        return ResponseEntity.ok(
                "成功"
        );
    }
}
