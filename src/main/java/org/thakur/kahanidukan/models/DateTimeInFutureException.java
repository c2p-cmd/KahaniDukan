package org.thakur.kahanidukan.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Datetime cannot be in the future")
public class DateTimeInFutureException extends RuntimeException {
    public DateTimeInFutureException() {
        super("Datetime cannot be in the future");
    }
}
