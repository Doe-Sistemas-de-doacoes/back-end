package com.labes.doe.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DonorReceiverDTO {
    private Integer id;
    private String name;
    private AddressDTO address;
}
