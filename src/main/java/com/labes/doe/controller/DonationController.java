package com.labes.doe.controller;

import com.labes.doe.dto.*;
import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.service.DonationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService service;

    @ApiOperation("Busca todas as doações por status. Se status for FINALIZADO retorna todas as doações encerradas senão retorna todas as doações disponiveis.")
    @GetMapping
    public Flux<DonationDTO> findAll( @RequestParam( defaultValue = "PENDENTE", required = false) String status ){
        return service.findAll( DonationStatus.valueOf(status) );
    }

    @ApiOperation(value = "Retorna a quantidade de doações encerradas.")
    @GetMapping( "/countFinishedDonations" )
    public Mono<Long> countFinishedDonations(){
        return service.countFinishedDonations();
    }

    @ApiOperation(value = "Salva uma nova doação.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DonationDTO> saveDonation(@RequestBody @Valid CreateNewDonationDTO body){
        return service.saveDonation(body);
    }

    @ApiOperation(value = "Atualiza uma doação.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<DonationDTO> updateDonation(@PathVariable Integer id, @RequestBody PatchDonationDTO body){
        return service.updateDonation(id, body);
    }

    @ApiOperation(value = "Apaga uma doação.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteDonation(@PathVariable Integer id){
        return service.deleteDonation(id);
    }

    @ApiOperation(value = "Recebe doações.")
    @PatchMapping("/receiveDonation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> receiveDonation(@RequestBody @Valid ReceiveDonationDTO body){
        return service.receiveDonation(body);
    }

}
