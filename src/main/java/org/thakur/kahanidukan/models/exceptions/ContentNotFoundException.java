package org.thakur.kahanidukan.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Content not found")
public class ContentNotFoundException extends RuntimeException {
}
