package com.labes.doe.service.impl;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.mapper.UserMapper;
import com.labes.doe.model.User;
import com.labes.doe.repository.UserRepository;
import com.labes.doe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<UserDTO> getUserById(Integer id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> saveUser(CreateNewUserDTO body) {
        User user = mapper.toSaveEntity( body );
        user.setId(null);
        return repository.save(user).map(mapper::toDto);
    }

    @Override
    public void deleteUserById(Integer id) {

    }

    @Override
    public Mono<UserDTO> updateUser(Integer id, UserDTO user) {
        return null;
    }
}
