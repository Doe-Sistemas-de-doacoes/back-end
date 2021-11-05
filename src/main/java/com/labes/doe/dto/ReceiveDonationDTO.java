package com.labes.doe.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReceiveDonationDTO {
    @NotNull( message = "As doações são obrigatórias.")
    private List<Integer> donations = new ArrayList<>();

    @NotNull( message = "O id do recebedor é obrigatório.")
    private Integer receiverId;
}
