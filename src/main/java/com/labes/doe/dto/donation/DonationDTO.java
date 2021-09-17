package com.labes.doe.dto.donation;

import com.labes.doe.model.donation.enumerations.DonationType;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DonationDTO {
    private Integer id;
    private Integer type;
    private String description;

}
