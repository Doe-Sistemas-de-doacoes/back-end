package com.labes.doe.service.impl;

import com.labes.doe.dto.*;
import com.labes.doe.exception.BusinessException;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.service.UserService;
import com.labes.doe.util.MessageUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.labes.doe.mapper.AddressMapper;
import com.labes.doe.repository.AddressRepository;
import com.labes.doe.service.AddressService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final AddressMapper addressMapper;
	private final UserService userService;

	@Override
	public Mono<AddressDTO> findById(Integer id) {
		return addressRepository.findById(id)
				.switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.ADDRESS_NOT_FOUND)))
				.map(addressMapper::toDto);
	}

	@Override
	public Flux<AddressDTO> findByUser() {
		return userService.getUser()
				.map(UserDTO::getId)
				.flatMapMany(addressRepository::findByUserId)
				.map(addressMapper::toDto);
	}

	@Override
	public Mono<UserAdressDTO> getUserAndAddress() {
		return userService.getUser()
				.flatMap(userDTO -> addressRepository.findByUserId(userDTO.getId())
						.map(addressMapper::toDto)
						.collectList()
						.map(address -> UserAdressDTO.builder()
							.id(userDTO.getId())
							.user(userDTO.getUser())
							.name(userDTO.getName())
							.address( address )
							.build()
						)
				);
	}

	@Override
	public Mono<AddressDTO> save(CreateNewAddressDTO body ) {
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
	public Mono<AddressDTO> update(Integer id, PutAddressDTO body) {
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
	public Mono<Void> delete(Integer id) {
		return userService.getUser()
				.flatMap(userDTO -> addressRepository.findByIdAndUserId(id,userDTO.getId()))
				.switchIfEmpty(Mono.error(new BusinessException("Endereço não encontrado! Verifique se o endereço pertence ao usuário.")))
				.flatMap(addressRepository::delete)
				.onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new BusinessException("Deleção não permitida! O endereço está relacionado com outros cadastros.")));
	}

}
