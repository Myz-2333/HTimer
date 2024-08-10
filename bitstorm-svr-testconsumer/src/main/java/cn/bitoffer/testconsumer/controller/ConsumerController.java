package cn.bitoffer.testconsumer.controller;

import cn.bitoffer.api.feign.DemoClient;
import cn.bitoffer.common.model.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/demo")
@Slf4j
public class ConsumerController {

    @Resource
    private DemoClient demoClient;

    @GetMapping("/example")
    public ResponseEntity<String> consumer() {
        String resultStr = demoClient.call("testconsumer");
        log.info("Result:" + resultStr);
        return ResponseEntity.ok(
                "result:"+resultStr
        );
    }
}
