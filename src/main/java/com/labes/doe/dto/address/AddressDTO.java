package com.labes.doe.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDTO {

	private Integer addressId;
	private String  neighborhood;
	private String  city;
	private String  state;
	private Integer number;
	private String  street;
	private Integer regionId;
	private Integer userId;
	
}
