package com.labes.doe.mapper;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.model.User;
import org.mapstruct.Mapper;

@Mapper()
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
    User toSaveEntity(CreateNewUserDTO userDTO);
}
