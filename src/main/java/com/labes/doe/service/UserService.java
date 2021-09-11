package com.labes.doe.service;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> getUserById(Integer id);
    Mono<UserDTO> saveUser(CreateNewUserDTO user);
    void deleteUserById(Integer id);
    Mono<UserDTO> updateUser(Integer id, UserDTO user);
}
