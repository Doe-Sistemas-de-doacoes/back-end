package com.labes.doe.controller;

import com.labes.doe.dto.CreateNewUserDTO;
import com.labes.doe.dto.UpdateUserDTO;
import com.labes.doe.dto.UserDTO;
import com.labes.doe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public Mono<UserDTO> getUser(){
        return service.getUserDTO();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserDTO> saveUser(@RequestBody @Valid CreateNewUserDTO user){
        return service.saveUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    public Mono<Void> deleteUser(){
        return service.deleteUser();
    }

    @PutMapping()
    public Mono<UserDTO> updateUser(@RequestBody UpdateUserDTO user){
        return service.updateUser(user);
    }

}
