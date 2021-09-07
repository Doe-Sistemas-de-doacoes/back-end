package com.labes.doe.service.impl;

import com.labes.doe.dto.UserDTO;
import com.labes.doe.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Mono<UserDTO> getUser() {
        return null;
    }
}
