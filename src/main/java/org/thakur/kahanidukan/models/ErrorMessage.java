package org.thakur.kahanidukan.models;

public record ErrorMessage(String errorMessage) {
    public ErrorMessage {
        if (errorMessage == null || errorMessage.isBlank() || errorMessage.equalsIgnoreCase("null")) {
            throw new IllegalArgumentException("Error message is required");
        }
    }
}
