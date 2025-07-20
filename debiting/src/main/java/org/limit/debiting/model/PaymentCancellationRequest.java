package org.limit.debiting.model;

import lombok.Data;

@Data
public class PaymentCancellationRequest {
    private Long paymentId;
}
