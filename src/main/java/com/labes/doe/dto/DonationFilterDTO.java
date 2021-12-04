package com.labes.doe.dto;

import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.model.enumeration.DonationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationFilterDTO {
    private DonationStatus status = DonationStatus.PENDENTE;
    private String description = "";
    private Set<DonationType> types = DonationType.getTypes();
    private Integer page = 0;
    private Integer size = 10;
}
