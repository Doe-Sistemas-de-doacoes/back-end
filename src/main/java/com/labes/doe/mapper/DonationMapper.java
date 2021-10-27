package com.labes.doe.mapper;

import com.labes.doe.dto.CreateNewDonationDTO;
import com.labes.doe.dto.DonationDTO;
import com.labes.doe.model.Donation;
import org.mapstruct.*;

@Mapper(componentModel = "Spring", config = IgnoreUnmappedMapperConfig.class, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface DonationMapper {
    DonationDTO toDto(Donation donation);
    Donation toEntity(CreateNewDonationDTO donation);
}
