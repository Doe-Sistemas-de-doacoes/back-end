package com.labes.doe.dto.address;

import com.labes.doe.model.enumeration.Region;
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
	private Integer id;
	private String  neighborhood;
	private String  city;
	private String  state;
	private Integer number;
	private String  street;
	private Region region;
	private Integer userId;
}
