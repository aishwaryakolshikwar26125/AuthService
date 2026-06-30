package org.example.userauthservice_may2026.exceptions;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String msg) {
        super(msg);
    }
}
