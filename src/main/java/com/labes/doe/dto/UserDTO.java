package com.labes.doe.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private Integer id;
    private String name;
}
