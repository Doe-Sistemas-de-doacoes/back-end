package com.labes.doe.dto.donation;

import com.labes.doe.model.enumeration.DonationType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DonationDTO {

    private Integer id;
    private DonationType typeOfDonation;
    private String description;
    private LocalDateTime datetimeOfCollection;
    private Integer donorId;

}
