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

    @ApiOperation(value = "Retorna a quantidade de doações conforme seu status.")
    @GetMapping( "/count" )
    public Mono<Long> count( @RequestParam( defaultValue = "FINALIZADO", required = false ) String status ){
        return service.count(  DonationStatus.valueOf(status) );
    }

    @ApiOperation(value = "Salva uma nova doação.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DonationDTO> save( @RequestBody @Valid CreateNewDonationDTO body ){
        return service.save(body);
    }

    @ApiOperation(value = "Atualiza uma doação.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<DonationDTO> update(@PathVariable Integer id, @RequestBody PatchDonationDTO body){
        return service.update(id, body);
    }

    @ApiOperation(value = "Apaga uma doação.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Integer id){
        return service.delete(id);
    }

    @ApiOperation(value = "Recebe doações.")
    @PatchMapping("/{id}/receive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<DonationDTO> receive(@PathVariable Integer id, @RequestBody ReceiveDonationDTO body){
        return service.receive(id, body);
    }

}
