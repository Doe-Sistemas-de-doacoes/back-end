package com.labes.doe.controller;

import com.labes.doe.dto.UserDTO;
import com.labes.doe.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
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
    public void getUser() throws Exception {
        Mono<UserDTO> user = Mono.just( UserDTO.builder().id(1).name("Fulano").build() );
        given(service.getUser()).willReturn(user);

        web.get().uri(USER_API)
               .accept(MediaType.APPLICATION_JSON)
               .exchange()
               .expectStatus().isOk()
               .expectBody(UserDTO.class)
               .value(UserDTO::getId, equalTo(1))
               .value(UserDTO::getName, equalTo("Fulano"));
    }
}