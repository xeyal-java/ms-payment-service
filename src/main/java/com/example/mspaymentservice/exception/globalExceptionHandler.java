package com.example.mspaymentservice.exception;

import com.example.mspaymentservice.dto.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {

    private final String PAYMENT_NOT_FOUND ="Payment.NotFound.Exception";

    @ExceptionHandler(PaymentNotFoundException.class)
    public ErrorResponse handlePaymentNotFoundException(PaymentNotFoundException e){
        return new ErrorResponse(PAYMENT_NOT_FOUND,e.getMessage());
    }
}
