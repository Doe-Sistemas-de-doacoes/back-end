package com.labes.doe.service.impl;

import com.labes.doe.dto.*;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.AddressMapper;
import com.labes.doe.mapper.UserMapper;
import com.labes.doe.model.enumeration.Profile;
import com.labes.doe.model.User;
import com.labes.doe.repository.UserRepository;
import com.labes.doe.service.AddressService;
import com.labes.doe.service.UserService;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;

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

    @Override
    public Mono<UserDTO> saveUser(CreateNewUserDTO body) {
        return Mono.just(body)
                .map( createNewUserDTO  -> {
                    var userEntity = mapper.toEntity(createNewUserDTO);
                    userEntity.setProfile(Profile.CLIE.getId());
                    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                    return userEntity;
                })
                .flatMap(repository::save)
                .flatMap(user -> Flux.fromIterable(body.getAddress())
                        .map( createNewAddressDTO -> {
                             createNewAddressDTO.setUserId(user.getId() );
                             return createNewAddressDTO;
                        })
                        .flatMap(addressService::saveAddress)
                        .collectList()
                        .map(addressDTOS -> {
                            var userDTO = mapper.toDto(user);
                            userDTO.setAddress(addressDTOS);
                            return userDTO;
                        })
                );
    }

    @Override
    public Mono<UserDTO> updateUser(Integer id, UpdateUserDTO body) {
        return addressService.findAddressByUser(id)
                .flatMap(addressDTO -> {
                    var putAddressDTO = addressMapper.toPutAddressDto(addressDTO);
                    return addressService.updateAddress(addressDTO.getId(), putAddressDTO);
                })
                .collectList()
                .flatMap( addressDTOS -> getUser(id)
                        .flatMap(user -> {
                            user.setName(body.getName());
                            user.setPassword(passwordEncoder.encode( body.getPassword()));
                            return repository.save(user);
                        })
                        .map(user -> {
                            var userDTO = mapper.toDto(user);
                            userDTO.setAddress(addressDTOS);
                            return userDTO;
                        })
                );

    }

    @Override
    public Mono<Void> deleteUserById(Integer id) {
        return addressService.findAddressByUser(id)
                .flatMap(addressDTO -> addressService.deleteAddress(addressDTO.getId()))
                .then(getUser(id).flatMap(repository::delete));
    }

    private Mono<User> getUser(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.USER_NOT_FOUND)));
    }
}
