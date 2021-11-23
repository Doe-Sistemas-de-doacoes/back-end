package com.labes.doe.service;

import com.labes.doe.dto.*;
import com.labes.doe.model.enumeration.DonationStatus;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface DonationService {
    Flux<DonationDTO> findAll(DonationStatus status);
    Flux<DonationDTO> findByUser();
    Mono<DonationDTO> save(Mono<FilePart> file, CreateNewDonationDTO body);
    Mono<DonationDTO> update(Integer id, PatchDonationDTO body);
    Mono<Void> delete(Integer id);
    Mono<DonationDTO> receive(Integer id, ReceiveDonationDTO body);
    Mono<Long> count( DonationStatus status );
}
