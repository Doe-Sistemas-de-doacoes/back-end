package com.labes.doe.service.donation;

import com.labes.doe.dto.donation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DonationService {
    Flux<DonationDTO> findAllDonation();
    Flux<DonationDTO> findAllDonationToReceive();
    Flux<DonationDTO> findAllDonationToDelivery();
    Mono<DonationDTO> saveDonation(CreateNewDonationDTO body);
    Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body);
    Mono<Void> deleteDonation(Integer id);
    Mono<Void> receiveDonation(ReceiveDonationDTO body);
    Mono<Void> updateDeliveryStatus(PatchStatusDonationDTO body);
    Mono<Void> updateCollectionStatus(PatchStatusDonationDTO body);
}
