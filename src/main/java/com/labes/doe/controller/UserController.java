package com.labes.doe.controller;

import com.labes.doe.dto.CreateNewUserDTO;
import com.labes.doe.dto.DonationDTO;
import com.labes.doe.dto.UpdateUserDTO;
import com.labes.doe.dto.UserDTO;
import com.labes.doe.service.AddressService;
import com.labes.doe.service.DonationService;
import com.labes.doe.dto.UserAdressDTO;
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
    private final AddressService addressService;

    @ApiOperation(value = "Retorna o usuário com seus endereços.")
    @GetMapping
    public Mono<UserAdressDTO> getUser(){
        return addressService.getUserAndAddress();
    }

    @ApiOperation(value = "Retorna todas as doações do usuário.")
    @GetMapping("/donations")
    public Flux<DonationDTO> getDonations(){
        return donationService.findByUser();
    }

    @ApiOperation(value = "Salva um novo usuário.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserDTO> save(@RequestBody @Valid CreateNewUserDTO user){
        return userService.save(user);
    }

    @ApiOperation(value = "Apaga um usuário.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    public Mono<Void> delete(){
        return userService.delete();
    }

    @ApiOperation(value = "Atualiza um usuário.")
    @PutMapping()
    public Mono<UserDTO> update(@RequestBody UpdateUserDTO user){
        return userService.update(user);
    }

}
