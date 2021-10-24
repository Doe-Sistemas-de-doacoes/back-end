package com.labes.doe.service.login.impl;

import com.labes.doe.dto.credential.CredentialDTO;
import com.labes.doe.dto.user.UserTokenDTO;
import com.labes.doe.exception.InvalidUsernamePasswordException;
import com.labes.doe.mapper.user.UserMapper;
import com.labes.doe.repository.user.UserRepository;
import com.labes.doe.service.login.LoginService;
import com.labes.doe.service.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Mono<UserTokenDTO> login(CredentialDTO credential) {
        return userRepository
                .findByUser(credential.getUsername())
                .switchIfEmpty(Mono.error(new InvalidUsernamePasswordException()))
                .filter(user -> bCryptPasswordEncoder.matches( credential.getPassword(), user.getPassword() ) )
                .switchIfEmpty(Mono.error(new InvalidUsernamePasswordException()))
                .map(user -> {
                    UserTokenDTO userTokenDTO = userMapper.toDtoToken(user);
                    userTokenDTO.setToken(jwtUtil.generateToken(user.getUser()));
                    return userTokenDTO;
                });
    }
}
