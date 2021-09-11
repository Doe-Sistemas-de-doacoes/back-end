package com.labes.doe.dto.user;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String user;
    private String name;
}
