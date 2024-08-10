package cn.bitoffer.xtimer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"cn.bitoffer"})
@EnableScheduling
@EnableAsync
public class XTimerApplication {

    public static void main(String[] args) {
        SpringApplication.run(XTimerApplication.class, args);
    }

}
