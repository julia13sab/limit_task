package org.limit.debiting.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.application.cache")
@Getter
@Setter
public class CacheProperties {
    private Long limit;
    private String schedule;
}
