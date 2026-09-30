package com.bwesound.identity.auth.api;

import com.bwesound.identity.auth.EmailAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.bwesound.identity.auth.InvalidCredentialsException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ProblemDetail handleEmailAlreadyInUse(
            EmailAlreadyInUseException exception
    ) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(
            InvalidCredentialsException exception
    ) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage()
        );
    }
}
