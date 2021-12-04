package com.labes.doe.controller;

import com.labes.doe.dto.*;
import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.service.AddressService;
import com.labes.doe.service.DonationService;
import com.labes.doe.service.UserService;
import com.labes.doe.service.security.impl.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
    public Mono<Page<DonationDTO>> getDonations( DonationFilterDTO filter ){
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    var user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
                    return donationService.findDonationsByUser(user.getId(), filter);
                });
    }

    @ApiOperation(value = "Retorna todas as reservas do usuário.")
    @GetMapping("/reservations")
    public Mono<Page<DonationDTO>> getReservations( DonationFilterDTO filter ){
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    var user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
                    return donationService.findReservationsByUser(user.getId(), filter);
                });
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
