package org.thakur.kahanidukan.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad sorting order. Use values 'asc', 'desc', or 'none'")
public class BadSortingOrderException extends RuntimeException {
    public BadSortingOrderException(String message) {
        super(message);
    }
}
