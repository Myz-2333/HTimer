package com.zdf.worker;

import com.zdf.worker.boot.AppLaunch;
import com.zdf.worker.boot.Launch;
import com.zdf.worker.task.Lark;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Launch l = new AppLaunch();
        // 启动worker
        l.start();
    }
}
