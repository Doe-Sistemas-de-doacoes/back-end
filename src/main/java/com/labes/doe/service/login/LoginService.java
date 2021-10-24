package com.labes.doe.service.login;

import com.labes.doe.dto.credential.CredentialDTO;
import com.labes.doe.dto.user.UserTokenDTO;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<UserTokenDTO> login(CredentialDTO credential);
}
