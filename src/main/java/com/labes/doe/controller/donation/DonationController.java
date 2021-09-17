package com.labes.doe.controller;

import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/donations")
public class DonationController {

    private final DonationService service;

    @GetMapping
    public Flux<DonationDTO> findAllDonations(){
        return service.findAllDonation();
    }

}
