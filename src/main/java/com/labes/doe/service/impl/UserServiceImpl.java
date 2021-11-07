package com.labes.doe.service.impl;

import com.labes.doe.dto.*;
import com.labes.doe.exception.BusinessException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @Override
    public Mono<UserDTO> getUser() {
        return getLoggedUser().flatMap(this::mapToUserDTO);
    }

    @Override
    public Mono<UserDTO> getUserById(Integer id) {
        return repository.findById(id).flatMap(this::mapToUserDTO);
    }

    @Override
    public Mono<UserDTO> saveUser(CreateNewUserDTO body) {
        return Mono.just(body.getUser())
                .flatMap(repository::findByUser)
                .flatMap(user -> Mono.error(new BusinessException(MessageUtil.USER_ALREADY_EXISTS.getMessage())))
                .switchIfEmpty(Mono.just(body))
                .flatMap( u -> Mono.just(body))
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
                .flatMap( addressDTOS -> getLoggedUser()
                        .flatMap(user -> {
                            if( isNotEmpty(body.getName()) ) {
                                user.setName(body.getName());
                            }
                            if( isNotEmpty(body.getPassword()) ) {
                                user.setPassword(passwordEncoder.encode(body.getPassword()));
                            }
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
        return getLoggedUser()
                .map(User::getId)
                .flatMap( id -> addressService
                        .findAddressByUser(id)
                        .map(AddressDTO::getId)
                        .flatMap(addressService::deleteAddress)
                        .thenEmpty(repository.deleteById( id ))
                );
    }

    private Mono<UserDTO> mapToUserDTO( User userIn ){
        return Mono.just(userIn)
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

    private Mono<User> getLoggedUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    var principal = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
                    return principal.getId();
                })
                .flatMap(repository::findById)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.USER_NOT_FOUND)));
    }
}
