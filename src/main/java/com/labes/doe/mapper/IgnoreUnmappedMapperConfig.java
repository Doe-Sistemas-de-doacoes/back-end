package com.labes.doe.mapper;

import com.labes.doe.dto.CreateNewUserDTO;
import com.labes.doe.dto.UserDTO;
import com.labes.doe.dto.UserTokenDTO;
import com.labes.doe.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IgnoreUnmappedMapperConfig {
}
