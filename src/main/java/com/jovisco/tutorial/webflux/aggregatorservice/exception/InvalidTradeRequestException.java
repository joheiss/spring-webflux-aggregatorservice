package com.jovisco.tutorial.webflux.aggregatorservice.exception;

public class InvalidTradeRequestException extends RuntimeException {
    public InvalidTradeRequestException(String message) {
        super(message);
    }
}
