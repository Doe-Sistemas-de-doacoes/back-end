package com.labes.doe.dto.donation;

import com.labes.doe.model.donation.enumerations.DonationType;
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
