package com.labes.doe.controller;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UpdateUserDTO;
import com.labes.doe.dto.user.UserDTO;
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
    private Mono<UserDTO> saveUser(@RequestBody CreateNewUserDTO user){
        return service.saveUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    private Mono<Void> deleteUser(@PathVariable Integer id){
        return service.deleteUserById(id);
    }

    @PutMapping("/{id}")
    private Mono<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UpdateUserDTO user){
        return service.updateUser(id, user);
    }

}
