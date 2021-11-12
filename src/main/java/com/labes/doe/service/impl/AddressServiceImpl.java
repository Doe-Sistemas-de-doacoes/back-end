package com.labes.doe.service.impl;

import com.labes.doe.exception.BusinessException;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.service.UserService;
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

import java.util.Objects;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final AddressMapper addressMapper;
	private final UserService userService;

	@Override
	public Flux<AddressDTO> findAddressByUser(Integer userId) {
		return userService.getUser()
				.flatMapMany(userDTO -> {
					if ( !userDTO.getId().equals(userId) ) {
						return Mono.error(new BusinessException("O id do usuário não pertence ao usuário logado!"));
					}
					return addressRepository.findByUserId(userId);
				})
				.map( addressMapper::toDto );
	}

	@Override
	public Flux<AddressDTO> findAllAddress() {
		return addressRepository.findAll().map( addressMapper::toDto );
	}

	@Override
	public Mono<AddressDTO> saveAddress( CreateNewAddressDTO body ) {
		return userService.getUser()
				.map(userDTO -> {
					var address = addressMapper.toEntity(body);
					address.setUserId( userDTO.getId() );
					return address;
				})
				.flatMap(addressRepository::save)
				.map( addressMapper::toDto );
	}

	@Override
	public Mono<AddressDTO> updateAddress(Integer id, PutAddressDTO body) {
		return userService.getUser()
				.flatMap(userDTO -> addressRepository.findByIdAndUserId(id,userDTO.getId()))
				.switchIfEmpty(Mono.error(new BusinessException("Endereço não encontrado. Verifique se o endereço pertence ao usuário!")))
				.flatMap(Address -> {
					Address.setNeighborhood( body.getNeighborhood() );
					Address.setCity( body.getCity() );
					Address.setState( body.getState() );
					Address.setNumber( body.getNumber() );
					Address.setStreet( body.getStreet() );
					Address.setRegion( body.getRegion() );
					return addressRepository.save( Address );
				})
				.map(addressMapper::toDto);
	}

	@Override
	public Mono<Void> deleteAddress(Integer id) {
		return userService.getUser()
				.flatMap(userDTO -> addressRepository.findByIdAndUserId(id,userDTO.getId()))
				.switchIfEmpty(Mono.error(new BusinessException("Endereço não encontrado. Verifique se o endereço pertence ao usuário!")))
				.flatMap(addressRepository::delete);
	}

}
