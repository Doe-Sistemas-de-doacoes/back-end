package com.labes.doe.controller;

import com.labes.doe.dto.user.CreateNewUserDTO;
import com.labes.doe.dto.user.UpdateUserDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    WebTestClient web;

    @MockBean
    private UserService service;

    public static final String USER_API = "/users";

    @Test
    @DisplayName("should return a user by id")
    public void getUserById() {
        Mono<UserDTO> user = Mono.just( UserDTO.builder().id(1).name("Fulano").build() );
        given(service.getUserById(any(Integer.class))).willReturn(user);

        WebTestClient.ResponseSpec response = web
                .get()
                .uri(USER_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response
               .expectStatus().isOk()
               .expectBody(UserDTO.class)
               .value(UserDTO::getId, equalTo(1))
               .value(UserDTO::getName, equalTo("Fulano"));
    }

    @Test
    @DisplayName("should save a user")
    public void saveUser()  {
        CreateNewUserDTO body = CreateNewUserDTO.builder().name("Fulano").user("fulano300").password("123").build();
        Mono<UserDTO> user = Mono.just(UserDTO.builder().id(1).name("Fulano").build());

        given(service.saveUser( any( CreateNewUserDTO.class ))).willReturn(user);

        WebTestClient.ResponseSpec response = web
                .post()
                .uri(USER_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange();

        response
                .expectStatus().isCreated()
                .expectBody(UserDTO.class)
                .value(UserDTO::getId, equalTo(1))
                .value(UserDTO::getName, equalTo("Fulano"));
    }

    @Test
    @DisplayName("should delete a user")
    public void deleteUser()  {
        WebTestClient.ResponseSpec response = web
                .delete()
                .uri(USER_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isNoContent();
    }

    @Test
    @DisplayName("should update a user")
    public void updateUser()  {
        UserDTO body = UserDTO.builder().id(1).name("Fulano").build();
        Mono<UserDTO> user = Mono.just(UserDTO.builder().id(1).name("Fulano").build());

        given(service.updateUser(any(Integer.class), any(UpdateUserDTO.class))).willReturn(user);

        WebTestClient.ResponseSpec response = web
                .put()
                .uri(USER_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange();

        response
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(UserDTO::getId, equalTo(1))
                .value(UserDTO::getName, equalTo("Fulano"));
    }


}