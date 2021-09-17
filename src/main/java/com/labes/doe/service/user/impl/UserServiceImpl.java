package com.labes.doe.service.user.impl;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UpdateUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.mapper.user.UserMapper;
import com.labes.doe.model.user.User;
import com.labes.doe.repository.user.UserRepository;
import com.labes.doe.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public Mono<UserDTO> saveUser(CreateNewUserDTO body) {
        User user = mapper.toEntity( body );
        return repository.save(user).map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteUserById(Integer id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<UserDTO> updateUser(Integer id, UpdateUserDTO body) {

        return getUserById(id)
            .map(mapper::toEntity)
            .flatMap(userFind -> {
                userFind.setName(body.getName());
                userFind.setPassword(body.getPassword());
                return repository.save(userFind);
            })
            .map(mapper::toDto);
    }

}
