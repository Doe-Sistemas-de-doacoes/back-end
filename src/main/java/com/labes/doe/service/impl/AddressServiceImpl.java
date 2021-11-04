package com.labes.doe.service.impl;

import com.labes.doe.exception.NotFoundException;
import com.labes.doe.util.MessageUtil;
import org.springframework.stereotype.Service;

import com.labes.doe.dto.AddressDTO;
import com.labes.doe.dto.CreateNewAddressDTO;
import com.labes.doe.dto.PutAddressDTO;
import com.labes.doe.mapper.AddressMapper;
import com.labes.doe.model.Address;
import com.labes.doe.repository.AddressRepository;
import com.labes.doe.service.AddressService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

	private final AddressRepository repository;
	private final AddressMapper     mapper;

	@Override
	public Flux<AddressDTO> findAddressByUser(Integer userId) {
		return repository.findByUserId(userId)
				.map( mapper::toDto );
	}

	@Override
	public Flux<AddressDTO> findAllAddress() {
		return repository.findAll().map( mapper::toDto );
	}

	@Override
	public Mono<AddressDTO> saveAddress( CreateNewAddressDTO body ) {
		return Mono.just(body)
				.map(mapper::toEntity)
				.flatMap(repository::save)
				.map( mapper::toDto );
	}

	@Override
	public Mono<AddressDTO> updateAddress(Integer addressId, PutAddressDTO body) {
		return getAddress(addressId)
			.flatMap(Address -> {
					Address.setNeighborhood( body.getNeighborhood() );
					Address.setCity( body.getCity() );
					Address.setState( body.getState() );
					Address.setNumber( body.getNumber() );
					Address.setStreet( body.getStreet() );
					Address.setRegion( body.getRegion() );
					return repository.save( Address );
				})
				.map(mapper::toDto);
	}

	@Override
	public Mono<Void> deleteAddress(Integer addressId) {
		return getAddress(addressId)
				.flatMap(repository::delete);
	}

	protected Mono<Address> getAddress(Integer id) {
		return repository.findById(id)
				.switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.ADDRESS_NOT_FOUND)));
	}
}
