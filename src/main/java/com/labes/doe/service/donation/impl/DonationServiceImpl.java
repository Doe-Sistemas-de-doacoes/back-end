package com.labes.doe.service.donation.impl;

import com.labes.doe.dto.donation.*;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.mapper.user.UserMapper;
import com.labes.doe.model.donation.Donation;
import com.labes.doe.model.donation.enumerations.DonationStatus;
import com.labes.doe.repository.donation.DonationRepository;
import com.labes.doe.service.donation.DonationService;
import com.labes.doe.service.user.UserService;
import com.labes.doe.util.MessageUtil;
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
        return repository
                .findByStatusCollectionAndReceiverIdNull(DonationStatus.FINALIZADO)
                .map( mapper::toDto );
    }

    @Override
    public Mono<DonationDTO> saveDonation(CreateNewDonationDTO body) {
        return userService.getUserById(body.getDonorId())
                .onErrorMap( unesed -> new NotFoundException(MessageUtil.DONOR_NOT_FOUND) )
                .map(unused -> mapper.toEntity(body))
                .flatMap(donation -> {
                    donation.setStatusCollection(DonationStatus.PENDENTE);
                    return repository.save(donation);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body) {
        return getDonation(id)
                .flatMap(donation -> {
                    donation.setTypeOfDonation(body.getTypeOfDonation());
                    donation.setDescription(body.getDescription());
                    return repository.save(donation);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteDonation(Integer id) {
        return getDonation(id)
                .flatMap(repository::delete);
    }

    @Override
    public Mono<Void> receiveDonation(ReceiveDonationDTO body) {
        return userService.getUserById( body.getReceiverId() )
                .onErrorMap( unesed -> new NotFoundException(MessageUtil.RECEIVE_NOT_FOUND) )
                .thenMany( Flux.fromIterable( body.getDonations()) )
                .flatMap(this::getDonation)
                .flatMap(donation -> {
                    donation.setReceiverId(body.getReceiverId());
                    donation.setStatusDelivery(DonationStatus.PENDENTE);
                    donation.setDatetimeOfDelivery(body.getDatetimeOfDelivery());
                    return repository.save(donation);
                })
                .then();
    }

    @Override
    public Mono<Void> updateDeliveryStatus(PatchStatusDonationDTO body) {
        return Flux.fromIterable(body.getDonations())
            .flatMap(this::getDonation)
            .flatMap(donation -> {
                donation.setStatusDelivery(DonationStatus.FINALIZADO);
                return repository.save(donation);
            })
            .then();
    }

    @Override
    public Mono<Void> updateCollectionStatus(PatchStatusDonationDTO body) {
        return Flux.fromIterable(body.getDonations())
                .flatMap(this::getDonation)
                .flatMap(donation -> {
                    donation.setStatusCollection(DonationStatus.FINALIZADO);
                    return repository.save(donation);
                })
                .then();
    }

    private Mono<Donation> getDonation(Integer id){
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.DONATION_NOT_FOUND)));
    }

}
