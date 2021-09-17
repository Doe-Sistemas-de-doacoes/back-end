package com.labes.doe.repository.donation;

import com.labes.doe.model.donation.Donation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DonationRepository extends ReactiveCrudRepository<Donation, Integer> { }
