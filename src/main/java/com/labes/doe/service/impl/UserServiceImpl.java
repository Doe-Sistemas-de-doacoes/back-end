package com.labes.doe.service.impl;

import com.labes.doe.dto.UserDTO;
import com.labes.doe.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Mono<UserDTO> getUserById(Integer id) {
        return null;
    }

    @Override
    public Mono<UserDTO> saveUser(UserDTO user) {
        return null;
    }

    @Override
    public void deleteUserById(Integer id) {

    }

    @Override
    public Mono<UserDTO> updateUser(Integer id, UserDTO user) {
        return null;
    }
}
