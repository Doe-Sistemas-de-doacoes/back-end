package com.labes.doe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CredentialDTO {

    @NotNull(message = "O nome do usuário é obrigatório.")
    private String username;

    @NotNull(message = "A senha é obrigatória.")
    private String password;
}
