package org.limit.debiting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerError(Exception ex) {
        return String.format("Internal error : %s", ex.getMessage());
    }

    @ExceptionHandler(value = {PaymentNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String productNotFoundException(PaymentNotFoundException ex) {
        return String.format("Payment not found: %s", ex.getMessage());
    }

}
