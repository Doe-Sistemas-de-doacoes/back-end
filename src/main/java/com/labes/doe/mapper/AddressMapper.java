package com.labes.doe.mapper;

import com.labes.doe.dto.PutAddressDTO;
import org.mapstruct.Mapper;

import com.labes.doe.dto.AddressDTO;
import com.labes.doe.dto.CreateNewAddressDTO;
import com.labes.doe.model.Address;
import org.mapstruct.*;

@Mapper(componentModel = "Spring", config = IgnoreUnmappedMapperConfig.class, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface AddressMapper {
	AddressDTO toDto( Address address);
	Address toEntity( CreateNewAddressDTO address );
	PutAddressDTO toPutAddressDto( AddressDTO addressDTO );
}
