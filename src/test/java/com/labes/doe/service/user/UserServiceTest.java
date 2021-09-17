package com.labes.doe.service.user;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UpdateUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.mapper.user.UserMapper;
import com.labes.doe.model.user.User;
import com.labes.doe.repository.user.UserRepository;
import com.labes.doe.service.user.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    UserService service;

    @MockBean
    UserRepository repository;

    @MockBean
    UserMapper mapper;



    @BeforeEach
    public void setUp(){
        service = new UserServiceImpl(repository, mapper);
    }

    @Test
    public void shouldReturnUserById(){

        final int id = 1;
        Mono<User> user = getMonoUser();
        UserDTO userDTO = getUserDTO();

        when(repository.findById(id)).thenReturn(user);

        when(mapper.toDto(any())).thenReturn(userDTO);

        service.getUserById(id)
                .as(StepVerifier::create)
                .expectNextMatches( u ->
                        u.getId().equals(id) &&
                        u.getUser().equals("Fulano5000") &&
                        u.getName().equals("fulano")
                )
                .verifyComplete();

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