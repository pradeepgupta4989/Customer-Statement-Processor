package com.bank.statement.Exception;

import com.bank.statement.constants.StatementsProcessorConstants;
import com.bank.statement.model.StatementProcessorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

   @Override
   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(StatementProcessorResponse.builder()
                       .result(StatementsProcessorConstants.BAD_REQUEST)
                       .build());
   }
}