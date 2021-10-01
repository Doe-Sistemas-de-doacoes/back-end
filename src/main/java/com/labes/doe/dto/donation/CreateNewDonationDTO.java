package com.labes.doe.dto.donation;

import com.labes.doe.model.donation.enumerations.DonationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CreateNewDonationDTO {
    private Integer donorId;
    private DonationType typeOfDonation;
    private String description;
    private Boolean isPickUpAtHome;
    private LocalDateTime datetimeOfCollection;

}
