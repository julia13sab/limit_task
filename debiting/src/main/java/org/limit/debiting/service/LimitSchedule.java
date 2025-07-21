package org.limit.debiting.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.limit.debiting.config.CacheProperties;
import org.limit.debiting.persistence.repository.CashLimitRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Log4j2
@AllArgsConstructor
public class LimitSchedule {

    private CacheProperties cacheProperties;

    private CashLimitRepository cashLimitRepository;

    @Scheduled(cron = "${spring.application.cache.schedule}")
    public void scheduleClearLimits() {
        final var limit = cacheProperties.getLimit();
        log.info("Запущено задание сброса денежных лимитов в начальное значение {}", limit);

        cashLimitRepository.updateLimits(new BigDecimal(limit));

        log.info("Задание сброса денежных лимитов завершено");
    }
}
