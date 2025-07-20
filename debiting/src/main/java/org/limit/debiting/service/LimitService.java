package org.limit.debiting.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.limit.debiting.config.CacheProperties;
import org.limit.debiting.exception.PaymentNotFoundException;
import org.limit.debiting.model.PaymentCancellationRequest;
import org.limit.debiting.model.PaymentConfirmationRequest;
import org.limit.debiting.model.PaymentRequest;
import org.limit.debiting.model.PaymentResponse;
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
    public PaymentResponse initPayment(PaymentRequest paymentRequest) {
        final var payment = cashLimitRepository.findById(paymentRequest.getUser())
                .orElseGet(() -> cashLimitRepository.save(CashLimit.builder().userId(paymentRequest.getUser()).limitValue(
                        new BigDecimal(cacheProperties.getLimit())).build()));

        var reserve = BigDecimal.ZERO;

        if (Boolean.TRUE.equals(paymentRequest.getWithReserve())) {
            final var reserves = cashReserveRepository.findAllByDateGreaterThanAndDateLessThanAndCancelIsFalse(DateUtils.startDay(), DateUtils.endDay());
            if (!CollectionUtils.isEmpty(reserves)) {
                reserve = reserves.stream().map(CashReserve::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        }

        final var reservePayment = cashReserveRepository.save(CashReserve.builder().date(OffsetDateTime.now()).amount(paymentRequest.getAmount())
                .userId(paymentRequest.getUser()).build());

        return PaymentResponse.builder().id(reservePayment.getId()).limit(payment.getLimitValue().subtract(reserve)).build();
    }

    @Transactional
    public void commitPayment(PaymentConfirmationRequest paymentRequest) {
        final var reserve = cashReserveRepository.findById(paymentRequest.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException(String.format("Платеж %S на найден!", paymentRequest.getPaymentId())));

        final var payment = cashLimitRepository.getById(reserve.getUserId());

        //TODO: Надо ли проверять случаи, что другие транзакции уже могли списать лишнее и нам не хватает лимита?
        payment.setLimitValue(payment.getLimitValue().subtract(reserve.getAmount()));

        final var updated = cashLimitRepository.save(payment);
        log.info("Платеж обновлен: {}", updated);
    }

    public void rollbackPayment(PaymentCancellationRequest paymentRequest) {
        final var payment = cashReserveRepository.findById(paymentRequest.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException(String.format("Платеж %S на найден!", paymentRequest.getPaymentId())));

        payment.setCancel(true);

        final var updated = cashReserveRepository.save(payment);

        log.info("Резерв на платеж обновлен: {}", updated);
    }
}
