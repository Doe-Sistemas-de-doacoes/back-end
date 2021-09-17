package com.labes.doe.service.donation;

import com.labes.doe.dto.donation.DonationDTO;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface DonationService {

    Flux<DonationDTO> findAllDonation();
}
