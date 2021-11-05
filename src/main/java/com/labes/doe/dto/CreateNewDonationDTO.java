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

    @NotNull(message = "O tipo de doação é obrigatório.")
    private DonationType typeOfDonation;

    @NotNull(message = "A descrição é obrigatória.")
    private String description;

    @NotNull(message = "A busca em casa é obrigatória.")
    private Boolean isPickUpAtHome;

    private LocalDateTime datetimeOfCollection;

}
