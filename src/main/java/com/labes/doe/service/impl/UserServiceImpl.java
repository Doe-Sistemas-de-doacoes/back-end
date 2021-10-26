package com.labes.doe.service.impl;

import com.labes.doe.dto.CreateNewUserDTO;
import com.labes.doe.dto.UpdateUserDTO;
import com.labes.doe.dto.UserDTO;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.UserMapper;
import com.labes.doe.model.enumeration.Profile;
import com.labes.doe.model.User;
import com.labes.doe.repository.UserRepository;
import com.labes.doe.service.AddressService;
import com.labes.doe.service.UserService;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final AddressService addressService;


    @Override
    public Mono<UserDTO> getUserById(Integer id) {
        return addressService.findAddressByUser(id)
                .collectList()
                .flatMap(address -> getUser(id)
                        .map( user -> {
                            var userDTO = mapper.toDto(user);
                            userDTO.setAddress(address);
                            return userDTO;
                        })
                );
    }

    @Transactional
    @Override
    public Mono<UserDTO> saveUser(CreateNewUserDTO body) {
        return Mono.just(body)
                .map(mapper::toEntity)
                .map(user -> {
                    user.setProfile(Profile.CLIE.getId());
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    return user;
                })
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> updateUser(Integer id, UpdateUserDTO body) {
        return getUser(id)
                .flatMap(userFind -> {
                    userFind.setName(body.getName());
                    userFind.setPassword(passwordEncoder.encode( body.getPassword()));
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
