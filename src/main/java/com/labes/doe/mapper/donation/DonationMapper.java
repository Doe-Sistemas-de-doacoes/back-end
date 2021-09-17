package com.labes.doe.mapper.donation;

import com.labes.doe.dto.donation.CreateNewDonationDTO;
import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.model.donation.Donation;
import org.mapstruct.*;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface DonationMapper {

  // @Mapping( source = "donation.type", target = "type" )
    DonationDTO toDto(Donation donation);

   // @Mapping( source = "donation.type.id", target = "type" )
    Donation toEntity(CreateNewDonationDTO donation);

}
