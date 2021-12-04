package com.labes.doe.repository;

import com.labes.doe.model.Donation;
import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.model.enumeration.DonationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface DonationRepository extends ReactiveCrudRepository<Donation, Integer> {
    Mono<Long> countByStatus(DonationStatus status);
    Mono<Donation> findByIdAndDonorId( Integer id, Integer donorId );

    Flux<Donation> findByDescriptionContainingAndStatusAndTypeInAndDonorIdIsNot(
            String description, DonationStatus status, Collection<DonationType> types, Integer donorId, Pageable pageable);

    Mono<Long> countByDescriptionContainingAndStatusAndTypeInAndDonorIdIsNot(
            String description, DonationStatus status, Collection<DonationType> types, Integer donorId );

    Flux<Donation> findByDescriptionContainingAndStatusAndTypeInAndDonorId(
            String description, DonationStatus status, Collection<DonationType> types, Integer donorId, Pageable pageable);
    Mono<Long> countByDescriptionContainingAndStatusAndTypeInAndDonorId(
            String description, DonationStatus status, Collection<DonationType> types, Integer donorId);

    Flux<Donation> findByDescriptionContainingAndStatusAndTypeInAndReceiverId(
            String description, DonationStatus status, Collection<DonationType> types, Integer receiverId, Pageable pageable);
    Mono<Long> countByDescriptionContainingAndStatusAndTypeInAndReceiverId(
            String description, DonationStatus status, Collection<DonationType> types, Integer receiverId);
}


