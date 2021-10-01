package com.labes.doe.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PatchAddressDTO {
	
	private String  neighborhood;
	private String  city;
	private String  state;
	private Integer number;
	private String street;
	private Integer regionId;
}
