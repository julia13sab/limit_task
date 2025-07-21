package org.limit.debiting.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.limit.debiting.config.CacheProperties;
import org.limit.debiting.exception.LimitNotFoundException;
import org.limit.debiting.model.OperationCancellationRequest;
import org.limit.debiting.model.OperationConfirmationRequest;
import org.limit.debiting.model.OperationRequest;
import org.limit.debiting.model.OperationResponse;
import org.limit.debiting.persistence.entity.CashLimit;
import org.limit.debiting.persistence.entity.CashReserve;
import org.limit.debiting.persistence.repository.CashLimitRepository;
import org.limit.debiting.persistence.repository.CashReserveRepository;
import org.limit.debiting.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
@Log4j2
public class LimitService {

    private final CashReserveRepository cashReserveRepository;
    private final CashLimitRepository cashLimitRepository;
    private final CacheProperties cacheProperties;

    @Transactional
    public OperationResponse initPayment(OperationRequest operationRequest) {
        final var limit = cashLimitRepository.findById(operationRequest.getUser())
                .orElseGet(() -> cashLimitRepository.save(CashLimit.builder().userId(operationRequest.getUser()).limitValue(
                        new BigDecimal(cacheProperties.getLimit())).build()));

        var reserve = BigDecimal.ZERO;

        if (Boolean.TRUE.equals(operationRequest.getWithReserve())) {
            final var reserves = cashReserveRepository.findAllByDateGreaterThanAndDateLessThanAndCancelIsFalse(DateUtils.startDay(), DateUtils.endDay());
            if (!CollectionUtils.isEmpty(reserves)) {
                reserve = reserves.stream().map(CashReserve::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        }

        final var reservePayment = cashReserveRepository.save(CashReserve.builder().date(OffsetDateTime.now()).amount(operationRequest.getAmount())
                .userId(operationRequest.getUser()).build());

        return OperationResponse.builder().id(reservePayment.getId()).limit(limit.getLimitValue().subtract(reserve)).build();
    }

    @Transactional
    public void commitPayment(OperationConfirmationRequest operationRequest) {
        final var reserve = cashReserveRepository.findById(operationRequest.getPaymentId())
                .orElseThrow(() -> new LimitNotFoundException(String.format("Платеж %S на найден!", operationRequest.getPaymentId())));

        final var limit = cashLimitRepository.getById(reserve.getUserId());

        limit.setLimitValue(limit.getLimitValue().subtract(reserve.getAmount()));

        final var updated = cashLimitRepository.save(limit);
        log.info("Платеж обновлен: {}", updated);
    }

    public void rollbackPayment(OperationCancellationRequest operationRequest) {
        final var limit = cashReserveRepository.findById(operationRequest.getPaymentId())
                .orElseThrow(() -> new LimitNotFoundException(String.format("Платеж %S на найден!", operationRequest.getPaymentId())));

        limit.setCancel(true);

        final var updated = cashReserveRepository.save(limit);

        log.info("Резерв на платеж обновлен: {}", updated);
    }
}
