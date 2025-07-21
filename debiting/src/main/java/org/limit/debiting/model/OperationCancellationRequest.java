package org.limit.debiting.model;

import lombok.Data;

@Data
public class OperationCancellationRequest {
    private Long paymentId;
}
