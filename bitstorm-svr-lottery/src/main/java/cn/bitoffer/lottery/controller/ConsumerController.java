//package cn.bitoffer.lottery.controller;
//
//import cn.bitoffer.api.feign.DemoClient;
//import cn.bitoffer.lottery.common.ResponseEntity;
//import cn.bitoffer.lottery.redis.RedisExample;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
//@RestController
//@RequestMapping("/example")
//@Slf4j
//public class ConsumerController {
//
//    @Resource
//    private DemoClient providerClient;
//
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Autowired
//    private RedisExample redisExample;
//
//    @GetMapping("/consumer")
//    public ResponseEntity<String> consumer() {
//        String resultStr = providerClient.call("hello");
//        //String resultStr = "";
//        System.out.println("AA:" + resultStr);
//        log.info("BB:" + resultStr);
//        return ResponseEntity.ok(
//                "result:"+resultStr
//        );
//    }
//
//    @GetMapping("/kafkaTest")
//    public ResponseEntity<String> kafkaTest() {
//        // 消息队列发送消息
//        kafkaTemplate.send("hello-world","来自Kafka的测试消息【ABAB】");
//        return ResponseEntity.ok(
//                "发送消息成功"
//        );
//    }
//
//    @GetMapping("/redisTest")
//    public ResponseEntity<String> redisTest() {
//        // 消息队列发送消息
//        redisExample.luaExample();
//        return ResponseEntity.ok(
//                "成功"
//        );
//    }
//}
