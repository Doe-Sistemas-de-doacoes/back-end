package com.labes.doe.controller.advice;

import com.labes.doe.exception.NotFoundException;
import com.labes.doe.exception.InvalidUsernamePasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<String> notFoundExcption(NotFoundException excption){
        return Mono.just( excption.getMessage() );
    }

    @ExceptionHandler(InvalidUsernamePasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<String> invalidUsernameOrPassword(InvalidUsernamePasswordException excption){
        return Mono.just( excption.getMessage() );
    }

}
