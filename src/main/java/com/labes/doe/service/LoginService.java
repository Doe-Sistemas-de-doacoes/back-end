package com.labes.doe.service;

import com.labes.doe.dto.CredentialDTO;
import com.labes.doe.dto.UserTokenDTO;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<UserTokenDTO> login(CredentialDTO credential);
}
