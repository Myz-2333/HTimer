package cn.bitoffer.xtimer.common.pool;

import cn.bitoffer.xtimer.common.conf.SchedulerAppConf;
import cn.bitoffer.xtimer.common.conf.TriggerAppConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncPool {

    @Autowired
    SchedulerAppConf schedulerAppConf;

    @Autowired
    TriggerAppConf triggerAppConf;

    @Bean(name = "schedulerPool")
    public Executor schedulerPoolExecutor() {
        log.info("start schedulerPoolExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(schedulerAppConf.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(schedulerAppConf.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(schedulerAppConf.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(schedulerAppConf.getNamePrefix());

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    @Bean(name = "triggerPool")
    public Executor triggerPoolExecutor() {
        log.info("start triggerPoolExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(triggerAppConf.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(triggerAppConf.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(triggerAppConf.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(triggerAppConf.getNamePrefix());

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}