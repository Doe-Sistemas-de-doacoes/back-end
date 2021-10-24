package com.labes.doe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PatchStatusDonationDTO {
    private List<Integer> donations = new ArrayList<>();
}
