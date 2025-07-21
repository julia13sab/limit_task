package org.limit.debiting.exception;

public class LimitNotFoundException extends RuntimeException {

    public LimitNotFoundException(String message) {
        super(message);
    }
}
