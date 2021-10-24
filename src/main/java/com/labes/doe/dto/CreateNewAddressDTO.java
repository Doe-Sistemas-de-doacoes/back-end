package com.labes.doe.dto;

import com.labes.doe.model.enumeration.Region;
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
	private Region region;
	private Integer userId;
}
