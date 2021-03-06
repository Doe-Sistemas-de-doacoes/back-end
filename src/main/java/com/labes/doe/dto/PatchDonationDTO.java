package com.labes.doe.dto;

import com.labes.doe.model.enumeration.DonationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PatchDonationDTO {
    private DonationType typeOfDonation;
    private String description;
}
