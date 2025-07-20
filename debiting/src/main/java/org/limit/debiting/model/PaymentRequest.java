package org.limit.debiting.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long user;
    private BigDecimal amount;
    private Boolean withReserve;
}
