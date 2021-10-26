package com.labes.doe.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class DonationAvailableDTO {
    private DonationDTO donation;
    private UserDTO donor;
}
