package com.labes.doe.controller.donation;

import com.labes.doe.dto.donation.CreateNewDonationDTO;
import com.labes.doe.dto.donation.DonationDTO;
import com.labes.doe.dto.donation.PatchDonationDTO;
import com.labes.doe.service.donation.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/donations")
public class DonationController {

    private final DonationService service;

    @GetMapping
    public Flux<DonationDTO> findAllDonations(){
        return service.findAllDonation();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DonationDTO> saveDonation(@RequestBody CreateNewDonationDTO body){
        return service.saveDonation(body);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<DonationDTO> updateDonation(@PathVariable Integer id, @RequestBody PatchDonationDTO body){
        return service.updateDonation(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteDonation(@PathVariable Integer id){
        return service.deleteDonation(id);
    }

}
