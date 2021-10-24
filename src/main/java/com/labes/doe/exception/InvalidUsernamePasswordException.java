package com.labes.doe.exception;

public class InvalidUsernamePasswordException extends RuntimeException {

    public InvalidUsernamePasswordException() {
        super("Usuário ou senha inválido!");
    }
}
