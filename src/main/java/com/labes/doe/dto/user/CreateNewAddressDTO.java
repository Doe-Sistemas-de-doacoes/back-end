package com.labes.doe.dto.user;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateNewAddressDTO {
   // private Integer id;
    private String neighborhood;
    private String city;
    private String state;
    private Integer number;
    private String street;

}
