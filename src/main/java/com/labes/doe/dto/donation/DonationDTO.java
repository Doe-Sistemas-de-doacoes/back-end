package com.labes.doe.dto.donation;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DonationDTO {
    private Integer id;
    private String description;

}
