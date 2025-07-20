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
        log.info("Запущено задание сброса денежных лимитов в начальное значение {}", cacheProperties.getLimit());

        final var limits = cashLimitRepository.findAll();

        log.info("Получено лимитов {}", limits.size());

        limits.forEach(limit -> limit.setLimitValue(new BigDecimal(cacheProperties.getLimit())));

        cashLimitRepository.saveAll(limits);

        log.info("Задание сброса денежных лимитов завершено");
    }
}
