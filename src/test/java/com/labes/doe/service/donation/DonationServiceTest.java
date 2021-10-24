package com.labes.doe.service.donation;

import com.labes.doe.dto.donation.CreateNewDonationDTO;
import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.dto.donation.PatchDonationDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.mapper.user.UserMapper;
import com.labes.doe.model.donation.Donation;
import com.labes.doe.model.user.User;
import com.labes.doe.repository.donation.DonationRepository;
import com.labes.doe.service.donation.impl.DonationServiceImpl;
import com.labes.doe.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.labes.doe.model.enumeration.DonationType.ROUPA;
import static com.labes.doe.service.user.UserServiceTest.getMonoUserDTO;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonationServiceTest {

    @InjectMocks
    DonationServiceImpl service;

    @Mock
    DonationRepository repository;

    @Mock
    UserService userService;

    @Mock
    DonationMapper mapper;

    @Mock
    UserMapper userMapper;

    @Test
    public void findDonations(){
        when(repository.findAll()).thenReturn(Flux.just(getDonation(1),getDonation(2)));
        when(userService.getUserById(any(Integer.class))).thenReturn( Mono.just( UserDTO.builder().build()) );
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(User.builder().build());
        when(mapper.toDto(any())).thenReturn( getDonationDTO(1) );

        service.findAllDonation()
            .as(StepVerifier::create)
            .expectNextCount(2)
            .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    public void saveDonation(){

        CreateNewDonationDTO body = new CreateNewDonationDTO(1, ROUPA,"teste", false, now());

        when(userService.getUserById(any(Integer.class))).thenReturn( getMonoUserDTO() );
        when(repository.save( any(Donation.class) ) ).thenReturn( Mono.just( getDonation(1)) );
        when(mapper.toEntity(any())).thenReturn( getDonation(1) );
        when(mapper.toDto(any())).thenReturn( getDonationDTO(1) );

        service
            .saveDonation( body )
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();

        verify(repository, times(1)).save( any(Donation.class)  );

    }

    @Test
    public void updateDonation(){

        when(repository.findById( any(Integer.class) ) ).thenReturn( Mono.just( getDonation(1)) );
        when(repository.save( any(Donation.class) ) ).thenReturn( Mono.just( getDonation(1)) );
        when(mapper.toDto(any())).thenReturn( getDonationDTO(1) );

        service
            .updateDonation( 1, new PatchDonationDTO(ROUPA,"teste"))
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();

        verify(repository, times(1)).findById( any(Integer.class) );
        verify(repository, times(1)).save( any(Donation.class) );
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