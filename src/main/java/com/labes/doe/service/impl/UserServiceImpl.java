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
import com.labes.doe.service.security.impl.UserDetailsImpl;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Mono<UserDTO> getUserDTO() {
        return getUser()
                .flatMap( user -> addressService.findAddressByUser(user.getId())
                        .collectList()
                        .map(address -> {
                            var userDTO = mapper.toDto(user);
                            userDTO.setAddress(address);
                            return userDTO;
                        })
                );
    }

    @Override
    public Mono<UserDTO> getUserById(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.USER_NOT_FOUND)))
                .flatMap( user -> addressService.findAddressByUser(user.getId())
                        .collectList()
                        .map(address -> {
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
    public Mono<UserDTO> updateUser( UpdateUserDTO body) {
        return Flux.fromIterable(body.getAddress())
                .flatMap(addressDTO -> {
                    var putAddressDTO = addressMapper.toPutAddressDto(addressDTO);
                    return addressService.updateAddress(addressDTO.getId(), putAddressDTO);
                })
                .collectList()
                .flatMap( addressDTOS -> getUser()
                        .flatMap(user -> {
                            user.setName(body.getName());
                            user.setUser(body.getUser());
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
    public Mono<Void> deleteUser() {
        return getUser()
                .map(User::getId)
                .flatMap( id -> addressService
                        .findAddressByUser(id)
                        .map(AddressDTO::getId)
                        .flatMap(addressService::deleteAddress)
                        .thenEmpty(repository.deleteById( id ))
                );
    }

    private Mono<User> getUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    var principal = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
                    return principal.getId();
                })
                .flatMap(repository::findById)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.USER_NOT_FOUND)));
    }
}
