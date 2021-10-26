package com.labes.doe.repository;

import com.labes.doe.model.Donation;
import com.labes.doe.model.enumeration.DonationStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DonationRepository extends ReactiveCrudRepository<Donation, Integer> {
    Flux<Donation> findByStatusCollectionAndReceiverIdNull(DonationStatus statusCollection );
    Flux<Donation> findByStatusDeliveryAndReceiverIdNotNull(DonationStatus statusDelivery);
}
