package com.labes.doe.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    private String user;
    private String name;
    private String password;
    private List<AddressDTO> address = new ArrayList<>();
}
