package com.labes.doe.mapper.donation;

import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.model.donation.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface DonationMapper {
    DonationDTO toDto(Donation donation);
}
