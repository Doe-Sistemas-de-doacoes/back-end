package com.labes.doe.service;

import com.labes.doe.dto.*;
import com.labes.doe.model.enumeration.DonationStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DonationService {
    Flux<DonationDTO> findAll(DonationStatus status);
    Flux<DonationDTO> findByUser();
    Mono<DonationDTO> saveDonation(CreateNewDonationDTO body);
    Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body);
    Mono<Void> deleteDonation(Integer id);
    Mono<Void> receiveDonation(ReceiveDonationDTO body);
    Mono<Long> countFinishedDonations();
}
