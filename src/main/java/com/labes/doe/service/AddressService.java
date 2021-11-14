package com.labes.doe.service;


import com.labes.doe.dto.AddressDTO;
import com.labes.doe.dto.CreateNewAddressDTO;
import com.labes.doe.dto.PutAddressDTO;

import com.labes.doe.dto.UserAdressDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AddressService {
	Mono< AddressDTO > findById(Integer id);
	Mono<UserAdressDTO> getUserAndAddress();
	Mono< AddressDTO > save(CreateNewAddressDTO body );
	Mono< AddressDTO > update(Integer addressId, PutAddressDTO body );
	Mono< Void > delete(Integer addressId );
	Flux<AddressDTO> findByUser();
}


