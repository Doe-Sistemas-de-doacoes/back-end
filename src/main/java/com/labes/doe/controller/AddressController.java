package com.labes.doe.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.labes.doe.dto.AddressDTO;
import com.labes.doe.dto.CreateNewAddressDTO;
import com.labes.doe.dto.PutAddressDTO;
import com.labes.doe.service.AddressService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressController {

	private final AddressService service;

    @ApiOperation(value = "retorna todos os endereços do usuário logado.")
    @GetMapping
    public Flux<AddressDTO> getAll(){
        return service.findByUser();
    }

    @ApiOperation(value = "Salva um novo endereço")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AddressDTO> save(@RequestBody @Valid CreateNewAddressDTO body){
        return service.save(body);
    }

    @ApiOperation(value = "Atualiza um endereço")
    @PutMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AddressDTO> update(@PathVariable Integer addressId, @RequestBody PutAddressDTO body){
        return service.update(addressId, body);
    }

    @ApiOperation(value = "Remove um endereço")
    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Integer addressId){
        return service.delete(addressId);
    }
}
