package com.labes.doe.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PatchDonationDTO {
    private Integer type;
    private String description;
}
