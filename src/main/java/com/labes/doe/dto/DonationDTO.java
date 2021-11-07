package com.labes.doe.dto;

import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.model.enumeration.DonationType;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DonationDTO {
    private Integer id;
    private String description;
    private DonationType typeOfDonation;
    private UserDTO donor;
    private UserDTO receiver;
    private Boolean isPickUpAtHome;
    private LocalDateTime datetimeOfCollection;
    private LocalDateTime datetimeOfDelivery;
    private DonationStatus statusDelivery;
    private DonationStatus statusCollection;
}
