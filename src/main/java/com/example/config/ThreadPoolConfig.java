package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class ThreadPoolConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        VisiableThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();

        //核心线程
        executor.setCorePoolSize(1);

        //最大线程
        executor.setMaxPoolSize(1);

        //设置默认线程名称
        executor.setThreadNamePrefix("exportReport-");

        //队列大小
        executor.setQueueCapacity(1000);

        return executor;
    }

}
