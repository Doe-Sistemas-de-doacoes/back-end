package com.labes.doe.service.user;

import com.labes.doe.dto.CreateNewUserDTO;
import com.labes.doe.dto.UpdateUserDTO;
import com.labes.doe.dto.UserDTO;
import com.labes.doe.mapper.UserMapper;
import com.labes.doe.model.User;
import com.labes.doe.repository.UserRepository;
import com.labes.doe.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl service;

    @Mock
    UserRepository repository;

    @Mock
    UserMapper mapper;

    @Test
    public void shouldReturnUserById(){

        when(repository.findById(1)).thenReturn(getMonoUser());
        when(mapper.toDto(any())).thenReturn(getUserDTO());

        service.getUserById(1)
            .as(StepVerifier::create)
            .expectNextMatches( u ->
                u.getId().equals(1) &&
                u.getUser().equals("Fulano5000") &&
                u.getName().equals("fulano")
            )
            .verifyComplete();

        verify(repository).findById( any(Integer.class) );
    }

    @Test
    public void shouldCreateNewUser(){
        CreateNewUserDTO createNewUserDTO = getCreateUserDTO();
        User userEntity = getUser();

        when(mapper.toEntity( any(CreateNewUserDTO.class) ) ).thenReturn( userEntity );
        when(repository.save( userEntity )).thenReturn( getMonoUser() );
        when(mapper.toDto( any() ) ).thenReturn( getUserDTO() );

        service.saveUser( createNewUserDTO )
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();

        verify(repository).save(userEntity);

    }

    @Test
    public void shouldUpdateUser(){
        final int id = 1;
        UpdateUserDTO userDTO = UpdateUserDTO.builder().name("Nome alterado").password("senha123").build();

        User userEntity = getUser();

        when(repository.findById(id)).thenReturn(getMonoUser());
        when(mapper.toEntity( any(UserDTO.class) ) ).thenReturn(userEntity);
        when(repository.save( userEntity )).thenReturn( getMonoUser() );
        when(mapper.toDto( any() ) ).thenReturn( getUserDTO() );

        service.updateUser( id, userDTO )
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        verify(repository).findById(id);
        verify(repository).save(userEntity);
    }

    @Test
    public void shouldDeleteUser(){
        final int id = 1;
        service.deleteUserById(id);

        verify(repository).deleteById(id);
    }

    public static Mono<User> getMonoUser() {
        return Mono.just( User.builder().id(1).name("fulano").password("123").user("Fulano5000").build() );
    }

    public static Mono<UserDTO> getMonoUserDTO() {
        return Mono.just( UserDTO.builder().id(1).name("fulano").user("Fulano5000").build() );
    }

    public static UserDTO getUserDTO() {
        return UserDTO.builder().id(1).name("fulano").user("Fulano5000").build();
    }

    public static CreateNewUserDTO getCreateUserDTO() {
        return CreateNewUserDTO.builder().name("fulano").user("Fulano5000").build();
    }

    public static User getUser() {
        return User.builder().id(1).name("fulano").user("Fulano5000").password("123").build();
    }
}