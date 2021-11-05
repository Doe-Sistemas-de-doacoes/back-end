package com.labes.doe.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateNewUserDTO {

    @NotNull(message = "O usuário é obrigatório.")
    private String user;

    @NotNull(message = "O nome é obrigatório.")
    private String name;

    @NotNull(message = "A senha é obrigatória.")
    private String password;

    private List<CreateNewAddressDTO> address = new ArrayList<>();
}
