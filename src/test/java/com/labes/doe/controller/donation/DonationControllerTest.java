package com.labes.doe.controller.donation;

import com.labes.doe.service.donation.DonationService;
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
import reactor.core.publisher.Flux;
import static com.labes.doe.service.donation.DonationServiceTest.getDonationDTO;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(DonationController.class)
@AutoConfigureMockMvc
public class DonationControllerTest {

    @Autowired
    WebTestClient web;

    @MockBean
    private DonationService service;

    public static final String DONATION_API = "/donations";

    @Test
    @DisplayName("should return list of donations")
    public void listDonations(){

        given( service.findAllDonation() ).willReturn( Flux.just( getDonationDTO(1) ,getDonationDTO(2) ) );

        WebTestClient.ResponseSpec response = web.get().uri(DONATION_API)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus()
                .isOk();
    }
}