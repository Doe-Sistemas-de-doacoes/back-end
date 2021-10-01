package com.labes.doe.service.address.impl;

import org.springframework.stereotype.Service;

import com.labes.doe.dto.address.AddressDTO;
import com.labes.doe.dto.address.CreateNewAddressDTO;
import com.labes.doe.dto.address.PatchAddressDTO;
import com.labes.doe.mapper.address.AddressMapper;
import com.labes.doe.model.address.Address;
import com.labes.doe.repository.address.AddressRepository;
import com.labes.doe.service.address.AddressService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

	private final AddressRepository repository;
	private final AddressMapper     mapper;

	@Override
	public Flux<AddressDTO> findAllAddress() {
		return repository.findAll().map( mapper::toDto );
	}

	@Override
	public Mono<AddressDTO> saveAddress( CreateNewAddressDTO body ) {
		Address addressEntity = mapper.toEntity( body );
		return repository.save( addressEntity ).map( mapper::toDto );
	}

	@Override
	public Mono<AddressDTO> updateAddress(Integer addressId, PatchAddressDTO body) {
		return repository.findById(addressId)
				.flatMap(Address -> {
					Address.setNeighborhood( body.getNeighborhood() );
					Address.setCity( body.getCity() );
					Address.setState( body.getState() );
					Address.setNumber( body.getNumber() );
					Address.setStreet( body.getStreet() );
					Address.setRegionId( body.getRegionId() );
					
					return repository.save( Address );
				})
				.map(mapper::toDto);
	}

	@Override
	public Mono<Void> deleteAddress(Integer addressId) {
		return repository.deleteById(addressId);
	}
}
