package com.labes.doe.dto;

import com.labes.doe.model.enumeration.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ModCheck;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateNewAddressDTO {

	@NotNull(message = "O bairro é obrigatório.")
	private String neighborhood;

	@NotNull(message = "A cidade é obrigatória.")
	private String city;

	@NotNull(message = "O estado é obrigatório.")
	private String state;

	@NotNull(message = "O número é obrigatório.")
	private String number;

	@NotNull(message = "A rua é obrigatória.")
	private String street;

	@NotNull(message = "A região é obrigatória.")
	private Region region;

}
