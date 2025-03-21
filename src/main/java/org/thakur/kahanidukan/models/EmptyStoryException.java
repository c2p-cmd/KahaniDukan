package org.thakur.kahanidukan.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Story is empty")
public class EmptyStoryException extends RuntimeException {
}
