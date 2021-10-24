package com.labes.doe.model.enumeration;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Profile {
    ADMI(1, "ROLE_ADMI"),
    CLIE(2, "ROLE_CLIE");

    private final int id;
    private final String role;

    Profile(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public static Profile toEnum(Integer id){
        return Arrays
                .stream(Profile.values())
                .filter(p -> p.id == id)
                .findFirst()
                .orElse(null);
    }
}
