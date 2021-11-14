package com.labes.doe.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class UserAdressDTO {
    private Integer id;
    private String user;
    private String name;
    private List<AddressDTO> address = new ArrayList<>();
}
