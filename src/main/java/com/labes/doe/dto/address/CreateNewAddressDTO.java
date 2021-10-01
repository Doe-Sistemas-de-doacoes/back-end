package com.labes.doe.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateNewAddressDTO {
	
	private Integer addressId;
	private String  neighborhood;
	private String  city;
	private String  state;
	private Integer number;
	private Integer street;
	private Integer regionId;
	
}
