package com.labes.doe.controller.login;

import com.labes.doe.dto.credential.CredentialDTO;
import com.labes.doe.dto.user.UserTokenDTO;
import com.labes.doe.service.login.LoginService;
import com.labes.doe.service.login.impl.LoginServiceImpl;
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
