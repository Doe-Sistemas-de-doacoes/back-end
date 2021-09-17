package com.labes.doe.service.impl;

import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.mapper.donation.DonationMapper;
import com.labes.doe.repository.DonationRepository;
import com.labes.doe.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;

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
