package com.labes.doe.service.impl;

import com.labes.doe.dto.*;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.DonationMapper;
import com.labes.doe.model.Donation;
import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.repository.DonationRepository;
import com.labes.doe.service.DonationService;
import com.labes.doe.service.UserService;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final UserService userService;

    @Override
    public Flux<DonationDTO> findAll(DonationStatus status) {
        return Mono.just(status)
                .flatMapMany(donationStatus -> {
                    if ( donationStatus.equals(DonationStatus.PENDENTE) )
                        return donationRepository.findByStatusCollectionAndReceiverIdNull( DonationStatus.FINALIZADO );
                    return donationRepository.findByStatusDeliveryAndReceiverIdNotNull( DonationStatus.FINALIZADO );
                })
                .flatMap(this::mapToDonationDTO);
    }

    @Override
    public Flux<DonationDTO> findByUser() {
        return userService.getUser()
                .flatMapMany(userDTO -> donationRepository.findAllByDonorIdOrReceiverId(userDTO.getId(), userDTO.getId()))
                .flatMap(this::mapToDonationDTO);
    }


    @Override
    public Mono<DonationDTO> saveDonation(CreateNewDonationDTO body) {
        return userService.getUser()
                .onErrorMap(unesed -> new NotFoundException(MessageUtil.DONOR_NOT_FOUND))
                .flatMap(userDTO -> {
                    var donationEntity = donationMapper.toEntity(body);
                    donationEntity.setDatetimeOfCollection(LocalDateTime.now());
                    donationEntity.setStatusDelivery(DonationStatus.PENDENTE);
                    donationEntity.setStatusCollection(DonationStatus.FINALIZADO);
                    donationEntity.setDonorId(userDTO.getId());
                    return donationRepository.save(donationEntity)
                            .map( donation -> {
                              var donationDTO = donationMapper.toDto(donation);
                              donationDTO.setDonor(userDTO);
                              return donationDTO;
                            });
                });

    }

    @Override
    public Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body) {
        return getDonation(id)
                .flatMap(donation -> {
                    donation.setTypeOfDonation(body.getTypeOfDonation());
                    donation.setDescription(body.getDescription());
                    return donationRepository.save(donation);
                })
                .flatMap(this::mapToDonationDTO);
    }

    @Override
    public Mono<Void> deleteDonation(Integer id) {
        return getDonation(id).flatMap(donationRepository::delete);
    }

    @Override
    public Mono<Void> receiveDonation(ReceiveDonationDTO body) {
        return userService.getUserById(body.getReceiverId())
                .onErrorMap(unesed -> new NotFoundException(MessageUtil.RECEIVE_NOT_FOUND))
                .thenMany(Flux.fromIterable(body.getDonations()))
                .flatMap(this::getDonation)
                .flatMap(donation -> {
                    donation.setReceiverId(body.getReceiverId());
                    donation.setStatusDelivery(DonationStatus.FINALIZADO);
                    donation.setDatetimeOfDelivery( LocalDateTime.now() );
                    return donationRepository.save(donation);
                })
                .then();
    }

    @Override
    public Mono<Long> countFinishedDonations() {
        return donationRepository.countByStatusDeliveryAndReceiverIdNotNull(DonationStatus.FINALIZADO);
    }

    private Mono<DonationDTO> mapToDonationDTO( Donation donation ){
        return Mono.zip( Mono.just(donation),
                    donation.getDonorId() != null ? userService.getUserById(donation.getDonorId()) : Mono.just(UserDTO.builder().build()),
                    donation.getReceiverId() != null ? userService.getUserById(donation.getReceiverId()) : Mono.just(UserDTO.builder().build()))
                .map(tuple -> {
                    var donationDTO = donationMapper.toDto(tuple.getT1());
                    var donor = tuple.getT2();
                    var receiver = tuple.getT3();

                    donationDTO.setDonor(donor);
                    donationDTO.setReceiver(receiver);

                    return donationDTO;
                });

    }

    protected Mono<Donation> getDonation(Integer id) {
        return donationRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.DONATION_NOT_FOUND)));
    }

}
