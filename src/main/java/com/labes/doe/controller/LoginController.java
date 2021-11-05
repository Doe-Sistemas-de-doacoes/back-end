package com.labes.doe.controller;

import com.labes.doe.dto.CredentialDTO;
import com.labes.doe.dto.UserTokenDTO;
import com.labes.doe.service.LoginService;
import com.labes.doe.service.security.JWTUtil;
import com.labes.doe.service.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;
    private final JWTUtil jwtUtil;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserTokenDTO> login(@RequestBody @Valid CredentialDTO credential) {
        return loginService.login(credential);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refreshToken")
    public Mono<String> refreshToken(ServerHttpResponse response) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    var principal = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
                    var token = jwtUtil.generateToken(principal.getUsername());
                    response.getHeaders().add("Authorization", "Bearer " + token);
                    return Mono.just(token);
                });
    }
}
