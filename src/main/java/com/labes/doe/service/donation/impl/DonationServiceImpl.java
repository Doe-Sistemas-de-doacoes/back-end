package com.labes.doe.service.donation.impl;

import com.labes.doe.dto.donation.CreateNewDonationDTO;
import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.dto.donation.PatchDonationDTO;
import com.labes.doe.dto.user.UserDTO;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.model.donation.Donation;
import com.labes.doe.repository.donation.DonationRepository;
import com.labes.doe.service.donation.DonationService;
import com.labes.doe.service.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository repository;
    private final UserService userService;
    private final DonationMapper mapper;

    @Override
    public Flux<DonationDTO> findAllDonation() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Mono<DonationDTO> saveDonation(CreateNewDonationDTO body) {
        userService.getUserById(body.getUserId());  //valida se o user existe
        Donation userEntity = mapper.toEntity(body);
       // userEntity.setType( body.getType().getId() );
        return repository.save(userEntity).map(mapper::toDto);
    }

    @Override
    public Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body) {
        return repository.findById(id)
                .flatMap(donation -> {
                    donation.setType(body.getType());
                    donation.setDescription(body.getDescription());
                    return repository.save(donation);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteDonation(Integer id) {
        return repository.deleteById(id);
    }


}
