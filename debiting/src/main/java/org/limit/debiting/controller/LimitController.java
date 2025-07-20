package org.limit.debiting.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.limit.debiting.model.PaymentCancellationRequest;
import org.limit.debiting.model.PaymentConfirmationRequest;
import org.limit.debiting.model.PaymentRequest;
import org.limit.debiting.model.PaymentResponse;
import org.limit.debiting.service.LimitService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limit/payment")
@AllArgsConstructor
@Log4j2
public class LimitController {

    private final LimitService limitService;

    @PostMapping("/init")
    public PaymentResponse initPayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("Получен запрос с инициализацией платежа: {}", paymentRequest);

        return limitService.initPayment(paymentRequest);
    }

    @PostMapping("/commit")
    public void commitPayment(@RequestBody PaymentConfirmationRequest paymentConfirmationRequest) {
        log.info("Получен запрос с подтверждением платежа: {}", paymentConfirmationRequest);

        limitService.commitPayment(paymentConfirmationRequest);
    }

    @PostMapping("/rollback")
    public void rollbackPayment(@RequestBody PaymentCancellationRequest paymentCancellationRequest) {
        log.info("Получен запрос с отменой платежа: {}", paymentCancellationRequest);

        limitService.rollbackPayment(paymentCancellationRequest);
    }
}
