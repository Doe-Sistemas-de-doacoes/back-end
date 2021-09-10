package com.labes.doe.controller;

import com.labes.doe.dto.UserDTO;
import com.labes.doe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    private Mono<UserDTO> getUser(@PathVariable Integer id){
        return service.getUserById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private Mono<UserDTO> saveUser(@RequestBody UserDTO user){
        return service.saveUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable Integer id){
        service.deleteUserById(id);
    }

    @PutMapping("/{id}")
    private Mono<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO user){
        return service.updateUser(id, user);
    }

}
