package com.labes.doe.controller;

import com.labes.doe.dto.CreateNewUserDTO;
import com.labes.doe.dto.DonationDTO;
import com.labes.doe.dto.UpdateUserDTO;
import com.labes.doe.dto.UserDTO;
import com.labes.doe.service.DonationService;
import com.labes.doe.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final DonationService donationService;

    @ApiOperation(value = "Retorna o usuário.")
    @GetMapping
    public Mono<UserDTO> getUser(){
        return userService.getUser();
    }

    @ApiOperation(value = "Retorna todas as doações do usuário.")
    @GetMapping("/donations")
    public Flux<DonationDTO> getDonations(){
        return donationService.findByUser();
    }

    @ApiOperation(value = "Salva um novo usuário.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserDTO> saveUser(@RequestBody @Valid CreateNewUserDTO user){
        return userService.saveUser(user);
    }

    @ApiOperation(value = "Apaga um usuário.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    public Mono<Void> deleteUser(){
        return userService.deleteUser();
    }

    @ApiOperation(value = "Atualiza um usuário.")
    @PutMapping()
    public Mono<UserDTO> updateUser(@RequestBody UpdateUserDTO user){
        return userService.updateUser(user);
    }

}
