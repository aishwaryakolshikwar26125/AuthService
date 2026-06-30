package org.example.userauthservice_may2026;

import org.example.userauthservice_may2026.exceptions.PasswordMismatchException;
import org.example.userauthservice_may2026.exceptions.UserAlreadyException;
import org.example.userauthservice_may2026.exceptions.UserNotSignedUpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({UserAlreadyException.class, UserNotSignedUpException.class, PasswordMismatchException.class})
    public ResponseEntity<String> handleException(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
