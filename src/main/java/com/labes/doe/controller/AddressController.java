package com.labes.doe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.labes.doe.dto.AddressDTO;
import com.labes.doe.dto.CreateNewAddressDTO;
import com.labes.doe.dto.PutAddressDTO;
import com.labes.doe.service.AddressService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class AddressController {

	private final AddressService service;

    @GetMapping
    public Flux<AddressDTO> findAllAddresss(){
        return service.findAllAddress();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AddressDTO> saveAddress(@RequestBody CreateNewAddressDTO body){
        return service.saveAddress(body);
    }

    @PutMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AddressDTO> updateAddress(@PathVariable Integer addressId, @RequestBody PutAddressDTO body){
        return service.updateAddress(addressId, body);
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAddress(@PathVariable Integer addressId){
        return service.deleteAddress(addressId);
    }
}
