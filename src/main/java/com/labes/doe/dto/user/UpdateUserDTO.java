package com.labes.doe.dto.user;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    private String name;
    private String password;
}
