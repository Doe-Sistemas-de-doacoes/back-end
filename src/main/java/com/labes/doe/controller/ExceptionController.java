package com.labes.doe.controller;

import com.labes.doe.exception.BusinessException;
import com.labes.doe.exception.InvalidTokenException;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.exception.InvalidUsernamePasswordException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<String> notFoundExcption(NotFoundException exception){
        return Mono.just( exception.getMessage() );
    }

    @ExceptionHandler(InvalidUsernamePasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<String> invalidUsernameOrPassword(InvalidUsernamePasswordException exception){
        return Mono.just( exception.getMessage() );
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<String> InvalidTokenException(InvalidTokenException exception){
        return Mono.just( exception.getMessage() );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Exception exception) {
        exception.printStackTrace();
        return Map.of("error", "Ocorreu um erro interno");
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleWebExchangeBindException(WebExchangeBindException exception) {
        return Map.of("errors", exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage))
        );
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> businessException(BusinessException exception) {
        exception.printStackTrace();
        return Map.of("error", exception.getMessage());
    }
}
