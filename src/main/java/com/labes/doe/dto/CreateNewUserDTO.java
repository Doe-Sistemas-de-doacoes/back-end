package com.labes.doe.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateNewUserDTO {
    private String user;
    private String name;
    private String password;
    private List<CreateNewAddressDTO> address = new ArrayList<>();
}
