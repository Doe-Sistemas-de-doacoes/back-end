package com.labes.doe.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class DonationToDeliveryDTO {
    private DonationDTO donation;
    private UserDTO receive;
}
