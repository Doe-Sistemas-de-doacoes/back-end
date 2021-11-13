package com.labes.doe.repository;

import com.labes.doe.model.Donation;
import com.labes.doe.model.enumeration.DonationStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DonationRepository extends ReactiveCrudRepository<Donation, Integer> {
    Flux<Donation> findByStatus(DonationStatus status);
    Mono<Long> countByStatus(DonationStatus status);
    Flux<Donation> findAllByDonorIdOrReceiverId( Integer donorId, Integer receiverId );
    Mono<Donation> findByIdAndDonorId( Integer id, Integer donorId );
}
