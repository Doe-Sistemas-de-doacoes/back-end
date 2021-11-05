package com.labes.doe.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    private String name;
    private String password;
    private List<AddressDTO> address = new ArrayList<>();
}
