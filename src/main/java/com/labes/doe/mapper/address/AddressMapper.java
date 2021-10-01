package com.labes.doe.mapper.address;

import org.mapstruct.Mapper;

import com.labes.doe.dto.address.AddressDTO;
import com.labes.doe.dto.address.CreateNewAddressDTO;
import com.labes.doe.model.address.Address;
import org.mapstruct.*;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface AddressMapper {

	AddressDTO toDto( Address address);

	Address toEntity( CreateNewAddressDTO address );
}
