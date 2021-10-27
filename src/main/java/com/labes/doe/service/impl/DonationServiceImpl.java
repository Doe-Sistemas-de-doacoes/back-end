package com.labes.doe.service.impl;

import com.labes.doe.dto.*;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.DonationMapper;
import com.labes.doe.model.Donation;
import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.repository.DonationRepository;
import com.labes.doe.service.AddressService;
import com.labes.doe.service.DonationService;
import com.labes.doe.service.UserService;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final UserService userService;

    @Override
    public Flux<DonationDTO> findAllDonationAvailable() {
        return donationRepository
                .findByStatusCollectionAndReceiverIdNull(DonationStatus.FINALIZADO)
                .flatMap(donation -> Flux.zip(
                        Flux.just(donation),
                        userService.getUserById(donation.getDonorId())
                ))
                .map(tuple -> {
                    var donation = tuple.getT1();
                    var donor = tuple.getT2();
                    var donationDTO = donationMapper.toDto(donation);

                    donationDTO.setDonor(donor);

                    return donationDTO;
                });
    }

    @Override
    public Flux<DonationDTO> findAllDonationToReceive() {
        return donationRepository
                .findByStatusCollectionAndReceiverIdNull(DonationStatus.PENDENTE)
                .map(donationMapper::toDto);
    }

    @Override
    public Flux<DonationDTO> findAllDonationToDelivery() {
        return donationRepository
                .findByStatusDeliveryAndReceiverIdNotNull(DonationStatus.PENDENTE)
                .flatMap(donation -> Flux.zip(
                        Flux.just(donation),
                        userService.getUserById(donation.getDonorId()),
                        userService.getUserById(donation.getReceiverId())
                ))
                .map(tuple -> {
                    var donation = tuple.getT1();
                    var donor = tuple.getT2();
                    var receiver = tuple.getT3();
                    var donationDTO = donationMapper.toDto(donation);

                    donationDTO.setDonor(donor);
                    donationDTO.setReceiver(receiver);

                    return donationDTO;
                });
    }

    @Override
    public Mono<DonationDTO> saveDonation(CreateNewDonationDTO body) {
        return userService.getUserById(body.getDonorId())
                .onErrorMap(unesed -> new NotFoundException(MessageUtil.DONOR_NOT_FOUND))
                .map(unused -> donationMapper.toEntity(body))
                .flatMap(donation -> {
                    donation.setStatusCollection(DonationStatus.FINALIZADO);
                    return donationRepository.save(donation);
                })
                .map(donationMapper::toDto);
    }

    @Override
    public Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body) {
        return getDonation(id)
                .flatMap(donation -> {
                    donation.setTypeOfDonation(body.getTypeOfDonation());
                    donation.setDescription(body.getDescription());
                    return donationRepository.save(donation);
                })
                .map(donationMapper::toDto);
    }

    @Override
    public Mono<Void> deleteDonation(Integer id) {
        return getDonation(id)
                .flatMap(donationRepository::delete);
    }

    @Override
    public Mono<Void> receiveDonation(ReceiveDonationDTO body) {
        return userService.getUserById(body.getReceiverId())
                .onErrorMap(unesed -> new NotFoundException(MessageUtil.RECEIVE_NOT_FOUND))
                .thenMany(Flux.fromIterable(body.getDonations()))
                .flatMap(this::getDonation)
                .flatMap(donation -> {
                    donation.setReceiverId(body.getReceiverId());
                    donation.setStatusDelivery(DonationStatus.PENDENTE);
                    donation.setDatetimeOfDelivery(body.getDatetimeOfDelivery());
                    return donationRepository.save(donation);
                })
                .then();
    }

    @Override
    public Mono<Void> updateDeliveryStatus(PatchStatusDonationDTO body) {
        return Flux.fromIterable(body.getDonations())
                .flatMap(this::getDonation)
                .flatMap(donation -> {
                    donation.setStatusDelivery(DonationStatus.FINALIZADO);
                    return donationRepository.save(donation);
                })
                .then();
    }

    @Override
    public Mono<Void> updateCollectionStatus(PatchStatusDonationDTO body) {
        return Flux.fromIterable(body.getDonations())
                .flatMap(this::getDonation)
                .flatMap(donation -> {
                    donation.setStatusCollection(DonationStatus.FINALIZADO);
                    return donationRepository.save(donation);
                })
                .then();
    }

    protected Mono<Donation> getDonation(Integer id) {
        return donationRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.DONATION_NOT_FOUND)));
    }

}
