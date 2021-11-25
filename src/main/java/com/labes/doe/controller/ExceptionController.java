package com.labes.doe.controller;

import com.labes.doe.exception.BusinessException;
import com.labes.doe.exception.InvalidTokenException;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.exception.InvalidUsernamePasswordException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono< Map<String, String> > notFoundExcption(NotFoundException exception){
        exception.printStackTrace();
        return Mono.just( Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(InvalidUsernamePasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono< Map<String, String> > invalidUsernameOrPassword(InvalidUsernamePasswordException exception){
        exception.printStackTrace();
        return Mono.just( Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono< Map<String, String> > InvalidTokenException(InvalidTokenException exception){
        exception.printStackTrace();
        return Mono.just( Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono< Map<String, String> > InvalidTokenException(ExpiredJwtException exception) {
        exception.printStackTrace();
        return Mono.just(Map.of("error", "Token jwt expirado!"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono< Map<String, String> > handleException(Exception exception) {
        exception.printStackTrace();
        return Mono.just( Map.of("error", "Ocorreu um erro interno.") );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono< Map<String, List<String>> > handleWebExchangeBindException(WebExchangeBindException exception) {
        return Mono.just(
                Map.of("errors", exception.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList())
                )
        );
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono< Map<String, String> > businessException(BusinessException exception) {
        exception.printStackTrace();
        return Mono.just( Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono< Map<String, String> > inputFileException(ServerWebInputException exception) {
        exception.printStackTrace();
        return Mono.just( Map.of("error", exception.getMessage() ) );
    }

}
