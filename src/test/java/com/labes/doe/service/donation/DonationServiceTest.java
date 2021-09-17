package com.labes.doe.service.donation;

import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.model.donation.Donation;
import com.labes.doe.repository.donation.DonationRepository;
import com.labes.doe.service.donation.impl.DonationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class DonationServiceTest {

    DonationService service;

    @MockBean
    DonationRepository repository;

    @MockBean
    DonationMapper mapper;

    @BeforeEach
    public void setUp(){
        service = new DonationServiceImpl(repository, mapper);
    }

    @Test
    public void findDonations(){

        when(repository.findAll()).thenReturn(Flux.just(getDonation(1),getDonation(2)));
        when(mapper.toDto(any())).thenReturn( getDonationDTO(1) );

        Flux<DonationDTO> donations = service.findAllDonation();

        donations
            .as(StepVerifier::create)
            .expectNextCount(2)
            .verifyComplete();

    }

    public static Donation getDonation(int id){
        return Donation.builder().id(id).description("teste").build();
    }
    public static DonationDTO getDonationDTO(int id){
        return DonationDTO.builder().id(id).description("teste").build();
    }
}