package com.labes.doe.service.impl;

import com.labes.doe.dto.AddressDTO;
import com.labes.doe.dto.CreateNewAddressDTO;
import com.labes.doe.dto.PutAddressDTO;
import com.labes.doe.dto.UserDTO;
import com.labes.doe.mapper.AddressMapper;
import com.labes.doe.model.Address;
import com.labes.doe.repository.AddressRepository;
import com.labes.doe.service.UserService;
import com.labes.doe.util.MessageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    AddressServiceImpl addressService;

    @Mock
    AddressMapper addressMapper;

    @Mock
    UserService userService;

    @Mock
    AddressRepository addressRepository;

    @Test
    @DisplayName("Deve salvar o endereço")
    public void save(){
        when(userService.getUser())
                .thenReturn( Mono.just( UserDTO.builder().id(1).build()) );

        when(addressMapper.toEntity(any(CreateNewAddressDTO.class)))
                .thenReturn(Address.builder().build());

        when(addressRepository.save(any(Address.class)))
                .thenReturn( Mono.just(Address.builder().build()) );

        when(addressMapper.toDto(any(Address.class)))
                .thenReturn(AddressDTO.builder().build());

        addressService.saveAddress(CreateNewAddressDTO.builder().build())
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("Deve retornar todos os endereços")
    public void findAll(){
        when(addressRepository.findAll())
                .thenReturn( Flux.just( Address.builder().build(), Address.builder().build()) );

        when(addressMapper.toDto(any(Address.class)))
                .thenReturn(AddressDTO.builder().build());

        addressService.findAllAddress()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();

        verify(addressRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar os endereços pelo código do usuário")
    public void findByUserId(){
        when(userService.getUser())
                .thenReturn( Mono.just( UserDTO.builder().id(1).build()) );

        when(addressRepository.findByUserId(any(Integer.class)))
                .thenReturn( Flux.just( Address.builder().build(), Address.builder().build()) );

        when(addressMapper.toDto(any(Address.class)))
                .thenReturn(AddressDTO.builder().build());

        addressService.findAddressByUser(1)
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();

        verify(addressRepository).findByUserId( any(Integer.class) );
    }

    @Test
    @DisplayName("Deve lançar erro se o código do usuário não for o mesmo código do usuário logado")
    public void shoundReturnErrorWhenUserIdIsNotEqualLoggedUserId(){
        when(userService.getUser())
                .thenReturn( Mono.just( UserDTO.builder().id(2).build()) );

        addressService.findAddressByUser(1)
                .as(StepVerifier::create)
                .expectErrorMessage("O id do usuário não pertence ao usuário logado!")
                .verify();

        verify(addressRepository, never()).findByUserId( any(Integer.class) );
    }

    @Test
    @DisplayName("Deve remover o endereço pelo seu código")
    public void remove(){
        when(userService.getUser())
                .thenReturn( Mono.just( UserDTO.builder().id(1).build()) );

        when(addressRepository.findByIdAndUserId(1, 1))
                .thenReturn( Mono.just(Address.builder().id(1).city("Guariba").state("SP").build()) );

        when(addressRepository.delete(any(Address.class)))
                .thenReturn( Mono.empty() );

        addressService.deleteAddress(1)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();

        verify(addressRepository).findByIdAndUserId( any(Integer.class), any(Integer.class) );
        verify(addressRepository).delete(any(Address.class));
    }

    @Test
    @DisplayName("Deve atualizar o endereço")
    public void update(){
        when(userService.getUser())
                .thenReturn( Mono.just( UserDTO.builder().id(1).build()) );

        when(addressRepository.findByIdAndUserId(1, 1))
                .thenReturn( Mono.just(Address.builder().id(1).city("Guariba").state("SP").build()) );

        when(addressRepository.save(any(Address.class)))
                .thenReturn( Mono.just(Address.builder().id(1).city("Rio de janeiro").state("RJ").build()) );

        when(addressMapper.toDto(any(Address.class)))
                .thenReturn(AddressDTO.builder().id(1).city("Rio de janeiro").state("RJ").build());

        addressService.updateAddress( 1, PutAddressDTO.builder().build())
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        verify(addressRepository).findByIdAndUserId( any(Integer.class), any(Integer.class) );
        verify(addressRepository).save( any(Address.class) );
    }

    @Test
    @DisplayName("Deve lançar erro se não encontrar o endereço do usuário")
    public void shouldReturnErrorWhenNotFoundAddressOfUserLogged(){
        when(userService.getUser())
                .thenReturn( Mono.just( UserDTO.builder().id(1).build()) );

        when(addressRepository.findByIdAndUserId(1, 1))
                .thenReturn( Mono.empty() );

        addressService.updateAddress( 1, PutAddressDTO.builder().build())
                .as(StepVerifier::create)
                .expectErrorMessage("Endereço não encontrado. Verifique se o endereço pertence ao usuário!")
                .verify();

        verify(addressRepository).findByIdAndUserId( any(Integer.class), any(Integer.class) );
    }

}