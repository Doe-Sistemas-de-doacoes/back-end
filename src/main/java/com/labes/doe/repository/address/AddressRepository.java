package com.labes.doe.repository.address;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.labes.doe.model.address.Address;

@Repository
public interface AddressRepository extends ReactiveCrudRepository<Address, Integer> {
}
