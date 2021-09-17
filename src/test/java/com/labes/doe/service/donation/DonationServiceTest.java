package com.labes.doe.service.donation;

import com.labes.doe.dto.donation.CreateNewDonationDTO;
import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.dto.donation.PatchDonationDTO;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.model.donation.Donation;
import com.labes.doe.repository.donation.DonationRepository;
import com.labes.doe.service.donation.impl.DonationServiceImpl;
import com.labes.doe.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.labes.doe.service.user.UserServiceTest.getMonoUserDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class DonationServiceTest {

    DonationService service;

    @MockBean
    DonationRepository repository;

    @MockBean
    UserService userService;

    @MockBean
    DonationMapper mapper;

    @BeforeEach
    public void setUp(){
        service = new DonationServiceImpl(repository,userService, mapper);
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

    @Test
    public void saveDonation(){

        when(userService.getUserById(any(Integer.class))).thenReturn( getMonoUserDTO() );
        when(repository.save( any(Donation.class) ) ).thenReturn( Mono.just( getDonation(1)) );
        when(mapper.toEntity(any())).thenReturn( getDonation(1) );
        when(mapper.toDto(any())).thenReturn( getDonationDTO(1) );

        Mono<DonationDTO> donation = service
                .saveDonation( new CreateNewDonationDTO(1,1,"teste"));

        donation
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void updateDonation(){

        when(repository.findById( any(Integer.class) ) ).thenReturn( Mono.just( getDonation(1)) );
        when(repository.save( any(Donation.class) ) ).thenReturn( Mono.just( getDonation(1)) );
        when(mapper.toDto(any())).thenReturn( getDonationDTO(1) );

        Mono<DonationDTO> donation = service
                .updateDonation( 1, new PatchDonationDTO(1,"teste"));

        donation
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void shouldDeleteDonation(){
        final int id = 1;
        service.deleteDonation(id);

        verify(repository).deleteById(id);
    }

    public static Donation getDonation(int id){
        return Donation.builder().id(id).description("teste").build();
    }
    public static DonationDTO getDonationDTO(int id){
        return DonationDTO.builder().id(id).description("teste").build();
    }

}