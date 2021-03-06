package com.labes.doe.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.labes.doe.model.Address;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AddressRepository extends ReactiveCrudRepository<Address, Integer> {
    Flux<Address> findByUserId(Integer userId);
    Mono<Address> findByIdAndUserId(Integer id, Integer userId );
}
