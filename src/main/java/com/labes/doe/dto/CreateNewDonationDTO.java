package com.labes.doe.dto;

import com.labes.doe.model.enumeration.DonationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CreateNewDonationDTO {

    @NotNull(message = "O tipo é obrigatório.")
    private DonationType type;

    @NotNull(message = "A descrição é obrigatória.")
    private String description;

    @NotNull(message = "É entrega é obrigatório.")
    private Boolean isDelivery;

    @NotNull(message = "O email é obrigatório.")
    private String email;

    @NotNull(message = "O telefone é obrigatório.")
    private String phone;

    private Integer addressId;

}
