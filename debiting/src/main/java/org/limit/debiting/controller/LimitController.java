package org.limit.debiting.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.limit.debiting.model.OperationCancellationRequest;
import org.limit.debiting.model.OperationConfirmationRequest;
import org.limit.debiting.model.OperationRequest;
import org.limit.debiting.model.OperationResponse;
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
    public OperationResponse initOperation(@RequestBody OperationRequest operationRequest) {
        log.info("Получен запрос с инициализацией операции: {}", operationRequest);

        return limitService.initPayment(operationRequest);
    }

    @PostMapping("/commit")
    public void commitOperation(@RequestBody OperationConfirmationRequest operationConfirmationRequest) {
        log.info("Получен запрос с подтверждением операции: {}", operationConfirmationRequest);

        limitService.commitPayment(operationConfirmationRequest);
    }

    @PostMapping("/rollback")
    public void rollbackOperation(@RequestBody OperationCancellationRequest operationCancellationRequest) {
        log.info("Получен запрос с отменой операции: {}", operationCancellationRequest);

        limitService.rollbackPayment(operationCancellationRequest);
    }
}
