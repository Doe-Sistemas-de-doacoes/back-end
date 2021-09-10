package com.labes.doe.service;

import com.labes.doe.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> getUserById(Integer id);
    Mono<UserDTO> saveUser(UserDTO user);
    void deleteUserById(Integer id);
    Mono<UserDTO> updateUser(Integer id, UserDTO user);
}
