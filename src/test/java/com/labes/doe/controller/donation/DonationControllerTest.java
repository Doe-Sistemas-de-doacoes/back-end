package com.labes.doe.controller.donation;

import com.labes.doe.controller.DonationController;
import com.labes.doe.dto.CreateNewDonationDTO;
import com.labes.doe.dto.DonationDTO;
import com.labes.doe.dto.PatchDonationDTO;
import com.labes.doe.service.DonationService;
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
import reactor.core.publisher.Mono;

import static com.labes.doe.model.enumeration.DonationType.ROUPA;
import static com.labes.doe.service.donation.DonationServiceTest.getDonationDTO;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
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
                .isOk()
                .expectBodyList(DonationDTO.class)
                .hasSize(2);
    }

    @Test
    @DisplayName("should create new donation")
    public void createDonation(){

        CreateNewDonationDTO body = new CreateNewDonationDTO(1, ROUPA,"teste", false, now());

        given( service.saveDonation( any(CreateNewDonationDTO.class) ) )
                .willReturn( Mono.just( getDonationDTO(1) ) );

        WebTestClient.ResponseSpec response = web.post().uri(DONATION_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange();

        response.expectStatus()
                .isCreated();
    }

    @Test
    @DisplayName("should patch donation")
    public void patchDonation(){

        PatchDonationDTO body = new PatchDonationDTO(ROUPA,"teste");

        given( service.updateDonation( any(Integer.class), any(PatchDonationDTO.class) ) )
                .willReturn( Mono.just( getDonationDTO(1) ) );

        WebTestClient.ResponseSpec response = web.patch().uri(DONATION_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange();

        response.expectStatus().isOk();
    }

    @Test
    @DisplayName("should delete donation")
    public void deleteDonation(){

        PatchDonationDTO body = new PatchDonationDTO(ROUPA,"teste");

        given( service.deleteDonation( any(Integer.class) ) )
                .willReturn( Mono.empty() );

        WebTestClient.ResponseSpec response = web.delete().uri(DONATION_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isNoContent();
    }
}