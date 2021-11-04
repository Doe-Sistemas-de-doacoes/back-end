package com.labes.doe.service;

import com.labes.doe.dto.CreateNewUserDTO;
import com.labes.doe.dto.UpdateUserDTO;
import com.labes.doe.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> getUserDTO();
    Mono<UserDTO> getUserById(Integer id);
    Mono<UserDTO> saveUser(CreateNewUserDTO user);
    Mono<Void> deleteUser();
    Mono<UserDTO> updateUser(UpdateUserDTO user);
}
