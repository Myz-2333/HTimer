package cn.bitoffer.shorturlx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Administrator
 */
@SpringBootApplication(scanBasePackages = {"cn.bitoffer"})
@EnableScheduling
public class ShortUrlXApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlXApplication.class, args);
    }

}
