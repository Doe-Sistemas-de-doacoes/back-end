package com.labes.doe.repository.donation;

import com.labes.doe.model.donation.Donation;
import com.labes.doe.model.donation.enumerations.DonationStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DonationRepository extends ReactiveCrudRepository<Donation, Integer> {
    Flux<Donation> findByStatusCollectionAndReceiverIdNull(DonationStatus statusCollection );
}
