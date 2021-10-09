package com.labes.doe.controller.advice;

import com.labes.doe.exception.BussinessException;
import com.labes.doe.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class AdviciController {

    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> bussinessExcption(BussinessException excption){
        return Mono.just( excption.getMessage() );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<String> notFoundExcption(NotFoundException excption){
        return Mono.just( excption.getMessage() );
    }
}
