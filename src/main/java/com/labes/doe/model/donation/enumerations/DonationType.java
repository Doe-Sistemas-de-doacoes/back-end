package com.labes.doe.model.donation.enumerations;

import lombok.Getter;

@Getter
public enum DonationType {
    ROUPA(1),
    ACESSORIO(2),
    ALIMENTO(3);

    private final int id;

    DonationType(int id) {
        this.id = id;
    }
}
