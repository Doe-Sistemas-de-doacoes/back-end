package com.labes.doe.service;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UpdateUserDTO;
import com.labes.doe.dto.user.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> getUserById(Integer id);
    Mono<UserDTO> saveUser(CreateNewUserDTO user);
    Mono<Void> deleteUserById(Integer id);
    Mono<UserDTO> updateUser(Integer id, UpdateUserDTO user);
}
