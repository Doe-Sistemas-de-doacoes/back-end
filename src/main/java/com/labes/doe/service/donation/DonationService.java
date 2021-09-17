package com.labes.doe.service.donation;

import com.labes.doe.dto.donation.CreateNewDonationDTO;
import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.dto.donation.PatchDonationDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DonationService {

    Flux<DonationDTO> findAllDonation();
    Mono<DonationDTO> saveDonation(CreateNewDonationDTO body);
    Mono<DonationDTO> updateDonation(Integer id, PatchDonationDTO body);
    Mono<Void> deleteDonation(Integer id);
}
