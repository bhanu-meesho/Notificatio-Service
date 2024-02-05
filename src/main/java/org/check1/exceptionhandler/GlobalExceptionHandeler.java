package org.check1;

import org.check1.entity.exceptions.BadRequestException;
import org.check1.entity.ErrorResponse;
import org.check1.entity.exceptions.ExceptionResponse;
import org.check1.entity.sms.FailSmsResponse;
import org.check1.entity.exceptions.FailSmsResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandeler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FailSmsResponseException.class)
    public ResponseEntity<ErrorResponse> handelFailSmsResponseException(FailSmsResponseException failSmsResponseException)
    {
        FailSmsResponse failSmsResponse = new FailSmsResponse("BAD_REQUEST 400 (Request id: "+failSmsResponseException.getRequestId()+")",failSmsResponseException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(failSmsResponse);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handelBadRequestException(BadRequestException exc)
    {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus_code(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(exc.getMessage());
        exceptionResponse.setTime(LocalDateTime.now());
        ErrorResponse errorResponse = new ErrorResponse(exceptionResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("Warnings", errors);
        ErrorResponse errorResponse = new ErrorResponse(result);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
