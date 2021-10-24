package com.labes.doe.controller;

import com.labes.doe.dto.CredentialDTO;
import com.labes.doe.dto.UserTokenDTO;
import com.labes.doe.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserTokenDTO> login(@RequestBody CredentialDTO credential){
        return loginService.login(credential);
    }
}
