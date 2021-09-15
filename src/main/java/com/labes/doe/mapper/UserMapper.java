package com.labes.doe.mapper;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UpdateUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(CreateNewUserDTO userDTO);
    User toEntity(UserDTO userDTO);
}
