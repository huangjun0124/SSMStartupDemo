package com.wishuok.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ExcutorConfig {
    private static Logger logger = Logger.getLogger(ExcutorConfig.class);

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        logger.info("start executor -->");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(50);
        //设置最大线程数
        executor.setMaxPoolSize(300);
        //设置队列大小
        executor.setQueueCapacity(300);
        //配置线程池的前缀
        executor.setThreadNamePrefix("async-service-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //设置空闲时间
        executor.setKeepAliveSeconds(60);
        //进行加载
        executor.initialize();
        return executor;
    }
}
