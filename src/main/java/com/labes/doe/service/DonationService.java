package com.labes.doe.service;

import com.labes.doe.dto.*;
import com.labes.doe.model.Donation;
import com.labes.doe.model.enumeration.DonationStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface DonationService {
    Mono<Page<DonationDTO>> findAll(Integer userId, DonationFilterDTO filterDTO);
    Mono<Page<DonationDTO>> findDonationsByUser(Integer userId, DonationFilterDTO filterDTO);
    Mono<Page<DonationDTO>> findReservationsByUser(Integer userId, DonationFilterDTO filterDTO);
    Mono<DonationDTO> save(Mono<FilePart> file, CreateNewDonationDTO body);
    Mono<DonationDTO> update(Integer id, PatchDonationDTO body);
    Mono<Void> delete(Integer id);
    Mono<DonationDTO> receive(Integer id, ReceiveDonationDTO body);
    Mono<Long> count( DonationStatus status );
}
