package com.labes.doe.mapper.user;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.dto.user.UserTokenDTO;
import com.labes.doe.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {
    UserDTO toDto(User user);
    UserTokenDTO toDtoToken(User user);
    User toEntity(CreateNewUserDTO userDTO);
    User toEntity(UserDTO userDTO);
}
