package com.fiap.soat12.tc_group_7.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StockUnavailableException extends RuntimeException {
    public StockUnavailableException(String message) {
        super(message);
    }
}
