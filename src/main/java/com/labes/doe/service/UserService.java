package com.labes.doe.service;

import com.labes.doe.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> getUser();
}
