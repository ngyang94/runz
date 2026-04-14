package com.ng.runz.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ng.runz.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> httpMessageNotReadableException(HttpMessageNotReadableException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("error",e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> illegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),null));
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> badRequestException(BadRequestException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),null));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgumentNotValidException(MethodArgumentNotValidException e){
        HashMap<String,String> errors = new HashMap<String,String>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.put(getJsonPropertyValue(fieldError),fieldError.getDefaultMessage());
        });
//        ObjectMapper om = new ObjectMapper();
//        System.out.println(om.writeValueAsString(e.getBindingResult().getFieldErrors()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error",errors));
    }

    private String getJsonPropertyValue(final FieldError error) {
        try {
            if (error.contains(ConstraintViolation.class)) {
                final ConstraintViolation<?> violation = error.unwrap(ConstraintViolation.class);
                final Field declaredField = violation.getRootBeanClass().getDeclaredField(error.getField());
                final JsonProperty annotation = declaredField.getAnnotation(JsonProperty.class);

                if (annotation != null && annotation.value() != null && !annotation.value().isEmpty()) {
                    return annotation.value();
                }
            }
        } catch (Exception ignored) {
        }

        return error.getField();
    }

}
