package org.example.userauthservice_may2026.exceptions;

public class UserNotSignedUpException extends RuntimeException {
    public UserNotSignedUpException(String msg) {
        super(msg);
    }
}
