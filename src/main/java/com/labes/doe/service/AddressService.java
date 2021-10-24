package com.labes.doe.service;


import com.labes.doe.dto.AddressDTO;
import com.labes.doe.dto.CreateNewAddressDTO;
import com.labes.doe.dto.PutAddressDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AddressService {
	
	Flux< AddressDTO > findAllAddress();
	Mono< AddressDTO > saveAddress( CreateNewAddressDTO body );
	Mono< AddressDTO > updateAddress( Integer addressId, PutAddressDTO body );
	Mono< Void > deleteAddress( Integer addressId );
}


