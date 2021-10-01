package com.labes.doe.service.donation.impl;

import com.labes.doe.dto.donation.*;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.mapper.user.UserMapper;
import com.labes.doe.model.donation.Donation;
import com.labes.doe.model.donation.enumerations.DonationStatus;
import com.labes.doe.repository.donation.DonationRepository;
import com.labes.doe.service.donation.DonationService;
import com.labes.doe.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository repository;
    private final UserService userService;
    private final DonationMapper mapper;
    private final UserMapper userMapper;

    @Override
    public Flux<DonationDTO> findAllDonation() {
        return repository
            .findByStatusCollectionAndReceiverIdNull(DonationStatus.FINALIZADO)
            .map( mapper::toDto );
    }

    @Override
    public Mono<DonationDTO> saveDonation(CreateNewDonationDTO body) {
        userService.getUserById(body.getDonorId()); //valida se o doador existe
        Donation donation = mapper.toEntity(body);
        donation.setStatusCollection(DonationStatus.PENDENTE);
        return repository.save(donation).map(mapper::toDto);
    }

    @Override
    public Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body) {
        return repository.findById(id)
                .flatMap(donation -> {
                    donation.setTypeOfDonation(body.getTypeOfDonation());
                    donation.setDescription(body.getDescription());
                    return repository.save(donation);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteDonation(Integer id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Void> receiveDonation(ReceiveDonationDTO body) {
        userService.getUserById(body.getReceiverId()); //valida se o doador existe

        body.getDonations().forEach( id -> {
            repository.findById(id)
                    .flatMap(donation -> {
                        donation.setReceiverId(body.getReceiverId());
                        donation.setStatusDelivery(DonationStatus.PENDENTE);
                        donation.setDatetimeOfDelivery(body.getDatetimeOfDelivery());
                        return repository.save(donation);
                    })
                    .subscribe();
        });

        return Mono.empty();
    }

    @Override
    public Mono<Void> updateDeliveryStatus(PatchStatusDonationDTO body) {

        body.getDonations().forEach( id -> {
           repository.findById(id)
            .flatMap(donation -> {
                donation.setStatusDelivery(DonationStatus.FINALIZADO);
                return repository.save(donation);
            })
            .subscribe();
        });

        return Mono.empty();
    }

    @Override
    public Mono<Void> updateCollectionStatus(PatchStatusDonationDTO body) {
        body.getDonations().forEach( id -> {
            repository.findById(id)
            .flatMap(donation -> {
                donation.setStatusCollection(DonationStatus.FINALIZADO);
                return repository.save(donation);
            })
            .subscribe();
        });

        return Mono.empty();
    }

}
