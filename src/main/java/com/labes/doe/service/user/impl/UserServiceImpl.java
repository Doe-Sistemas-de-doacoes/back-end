package com.labes.doe.service.user.impl;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UpdateUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.user.UserMapper;
import com.labes.doe.model.user.User;
import com.labes.doe.repository.user.UserRepository;
import com.labes.doe.service.user.UserService;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<UserDTO> getUserById(Integer id) {
        return getUser(id)
                .map(mapper::toDto);
    }

    @Transactional
    @Override
    public Mono<UserDTO> saveUser(CreateNewUserDTO body) {
        return Mono.just(body)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> updateUser(Integer id, UpdateUserDTO body) {
        return getUser(id)
                .flatMap(userFind -> {
                    userFind.setName(body.getName());
                    userFind.setPassword(body.getPassword());
                    return repository.save(userFind);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteUserById(Integer id) {
        return getUser(id)
                .flatMap(repository::delete);
    }

    private Mono<User> getUser(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.USER_NOT_FOUND)));
    }
}
