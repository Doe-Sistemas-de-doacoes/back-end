package com.labes.doe.dto;

import lombok.*;

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
}
