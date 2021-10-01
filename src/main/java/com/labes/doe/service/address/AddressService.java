package com.labes.doe.service.address;


import com.labes.doe.dto.address.AddressDTO;
import com.labes.doe.dto.address.CreateNewAddressDTO;
import com.labes.doe.dto.address.PutAddressDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AddressService {
	
	Flux< AddressDTO > findAllAddress();
	Mono< AddressDTO > saveAddress( CreateNewAddressDTO body );
	Mono< AddressDTO > updateAddress( Integer addressId, PutAddressDTO body );
	Mono< Void > deleteAddress( Integer addressId );
}


