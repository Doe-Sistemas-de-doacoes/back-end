package com.labes.doe.model.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum DonationType {
    ROUPA,
    ACESSORIO,
    ALIMENTO;

    public static Set<DonationType> getTypes(){
        return Arrays.stream(DonationType.values()).collect(Collectors.toSet());
    }
}
