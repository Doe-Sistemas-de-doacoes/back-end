package com.labes.doe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReceiveDonationDTO {
    private List<Integer> donations = new ArrayList<>();
    private Integer receiverId;
}
