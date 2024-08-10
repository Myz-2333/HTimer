package cn.bitoffer.leaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author leaf
 */
@SpringBootApplication(scanBasePackages = {"cn.bitoffer"})
public class LeafApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeafApplication.class, args);
    }

}
