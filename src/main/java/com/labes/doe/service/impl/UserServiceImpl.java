package com.labes.doe.service.impl;

import com.labes.doe.dto.*;
import com.labes.doe.exception.BusinessException;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.UserMapper;
import com.labes.doe.model.enumeration.Profile;
import com.labes.doe.model.User;
import com.labes.doe.repository.UserRepository;
import com.labes.doe.service.UserService;
import com.labes.doe.service.security.impl.UserDetailsImpl;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<UserDTO> getUser() {
        return getLoggedUser().map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> getUserById(Integer id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> saveUser(CreateNewUserDTO newUserDTO) {
        return Mono.just(newUserDTO.getUser())
                .flatMap(repository::findByUser)
                .flatMap(user -> Mono.error(new BusinessException(MessageUtil.USER_ALREADY_EXISTS.getMessage())))
                .switchIfEmpty(Mono.just(newUserDTO))
                .flatMap( createNewUserDTO  -> {
                    var userEntity = mapper.toEntity(newUserDTO);
                    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                    return repository.save( userEntity );
                })
               .map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> updateUser( UpdateUserDTO body) {
        return getLoggedUser()
                .flatMap(user -> {
                    if( isNotEmpty(body.getName()) ) user.setName(body.getName());
                    if( isNotEmpty(body.getPassword()) ) user.setPassword(passwordEncoder.encode(body.getPassword()));
                    return repository.save(user);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteUser() {
        return getLoggedUser().flatMap(repository::delete);
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
