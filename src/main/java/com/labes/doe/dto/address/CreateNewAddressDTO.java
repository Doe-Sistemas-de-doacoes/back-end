package com.labes.doe.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateNewAddressDTO {
	private String  neighborhood;
	private String  city;
	private String  state;
	private Integer number;
	private String street;
	private Integer regionId;
	private Integer userId;
}
