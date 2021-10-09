package com.labes.doe.util;

import lombok.Getter;

@Getter
public enum MessageUtil {
    USER_NOT_FOUND("Usuario não encontrado!"),
    RECEIVE_NOT_FOUND("Recebedor não encontrado!"),
    DONOR_NOT_FOUND("Doador não encontrado!"),
    DONATION_NOT_FOUND("Doação não encontrada!"),
    ADDRESS_NOT_FOUND("Endereço não encontrado!");

    MessageUtil(String message) {
        this.message = message;
    }

    private String message;
}
