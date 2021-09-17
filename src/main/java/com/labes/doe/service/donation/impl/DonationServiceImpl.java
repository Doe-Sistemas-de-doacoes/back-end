package com.labes.doe.service.donation.impl;

import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.repository.donation.DonationRepository;
import com.labes.doe.service.donation.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository repository;
    private final DonationMapper mapper;

    @Override
    public Flux<DonationDTO> findAllDonation() {
        return repository.findAll().map(mapper::toDto);
    }
}
