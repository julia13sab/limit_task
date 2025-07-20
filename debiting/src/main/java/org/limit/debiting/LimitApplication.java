package org.limit.debiting;

import org.limit.debiting.config.CacheProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(value = {CacheProperties.class})
public class LimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitApplication.class, args);
    }
}
