package com.labes.doe.dto;

import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.model.enumeration.DonationType;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DonationDTO {
    private Integer id;
    private String description;
    private DonationType type;
    private DonationStatus status;
    private DonorReceiverDTO donor;
    private DonorReceiverDTO receiver;
    private Boolean isDelivery;
    private String email;
    private String phone;
    private LocalDateTime date;
    private String imageSrc;
}
