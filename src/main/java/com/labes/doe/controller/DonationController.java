package com.labes.doe.controller;

import com.labes.doe.dto.*;
import com.labes.doe.service.DonationService;
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
    public Flux<DonationAvailableDTO> findAllDonationsAvailable(){
        return service.findAllDonationAvailable();
    }


    @GetMapping( "/findAllDonationToReceive" )
    public Flux<DonationDTO> findAllDonationToReceive(){
        return service.findAllDonationToReceive();
    }

    @GetMapping( "/findAllDonationToDelivery" )
    public Flux<DonationToDeliveryDTO> findAllDonationToDelivery(){
        return service.findAllDonationToDelivery();
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

    @PatchMapping("/receiveDonation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> receiveDonation(@RequestBody ReceiveDonationDTO body){
        return service.receiveDonation(body);
    }

    @PatchMapping("/deliveryStatus")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateDeliveryStatus(@RequestBody PatchStatusDonationDTO body){
        return service.updateDeliveryStatus(body);
    }

    @PatchMapping("/collectionStatus")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateCollectionStatus(@RequestBody PatchStatusDonationDTO body){
        return service.updateCollectionStatus(body);
    }

}
