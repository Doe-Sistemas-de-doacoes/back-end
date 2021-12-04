package com.labes.doe.service.impl;

import com.labes.doe.dto.*;
import com.labes.doe.exception.BusinessException;
import com.labes.doe.exception.NotFoundException;
import com.labes.doe.mapper.DonationMapper;
import com.labes.doe.model.Donation;
import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.repository.DonationRepository;
import com.labes.doe.service.AddressService;
import com.labes.doe.service.DonationService;
import com.labes.doe.service.S3Service;
import com.labes.doe.service.UserService;
import com.labes.doe.service.security.impl.UserDetailsImpl;
import com.labes.doe.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final UserService userService;
    private final AddressService addressService;
    private final S3Service s3Service;

    @Override
    public Mono<Page<DonationDTO>> findAll(Integer userId, DonationFilterDTO filterDTO) {
        return donationRepository.findByDescriptionContainingAndStatusAndTypeInAndDonorIdIsNot(
                        filterDTO.getDescription(),
                        filterDTO.getStatus(),
                        filterDTO.getTypes(),
                        userId,
                        PageRequest.of(filterDTO.getPage(), filterDTO.getSize())
                )
                .flatMap(this::mapToDonationDTO)
                .collectList()
                .flatMap(donationDTOS -> donationRepository
                        .countByDescriptionContainingAndStatusAndTypeInAndDonorIdIsNot(
                                filterDTO.getDescription(),
                                filterDTO.getStatus(),
                                filterDTO.getTypes(),
                                userId
                        )
                        .map(count -> new PageImpl<DonationDTO>(
                                donationDTOS,
                                PageRequest.of(filterDTO.getPage(), filterDTO.getSize()),
                                count
                        ))
                );
    }

    @Override
    public Mono<Page<DonationDTO>> findDonationsByUser(Integer userId, DonationFilterDTO filterDTO) {
        return donationRepository.findByDescriptionContainingAndStatusAndTypeInAndDonorId(
                        filterDTO.getDescription(),
                        DonationStatus.PENDENTE,
                        filterDTO.getTypes(),
                        userId,
                        PageRequest.of(filterDTO.getPage(), filterDTO.getSize())
                )
                .flatMap(this::mapToDonationDTO)
                .collectList()
                .flatMap(donationDTOS -> donationRepository
                        .countByDescriptionContainingAndStatusAndTypeInAndDonorId(
                                filterDTO.getDescription(),
                                DonationStatus.PENDENTE,
                                filterDTO.getTypes(),
                                userId
                        )
                        .map(count -> new PageImpl<DonationDTO>(
                                donationDTOS,
                                PageRequest.of(filterDTO.getPage(), filterDTO.getSize()),
                                count
                        ))
                );
    }

    @Override
    public Mono<Page<DonationDTO>> findReservationsByUser(Integer userId, DonationFilterDTO filterDTO) {
        return donationRepository.findByDescriptionContainingAndStatusAndTypeInAndReceiverId(
                        filterDTO.getDescription(),
                        DonationStatus.FINALIZADO,
                        filterDTO.getTypes(),
                        userId,
                        PageRequest.of(filterDTO.getPage(), filterDTO.getSize())
                )
                .flatMap(this::mapToDonationDTO)
                .collectList()
                .flatMap(donationDTOS -> donationRepository
                        .countByDescriptionContainingAndStatusAndTypeInAndReceiverId(
                                filterDTO.getDescription(),
                                DonationStatus.FINALIZADO,
                                filterDTO.getTypes(),
                                userId
                        )
                        .map(count -> new PageImpl<DonationDTO>(
                                donationDTOS,
                                PageRequest.of(filterDTO.getPage(), filterDTO.getSize()),
                                count
                        ))
                );
    }

    @Override
    public Mono<Long> count(DonationStatus status) {
        return donationRepository.countByStatus(status);
    }

    @Override
    public Mono<DonationDTO> save(Mono<FilePart> file, CreateNewDonationDTO createNewDonationDTO) {
        return userService.getUser()
                .flatMap(userDTO -> {
                    // se o doador escolheu que vai levar até o local e o recebedor não passou o seu endereço então lança a exceção
                    if (!createNewDonationDTO.getIsDelivery() && createNewDonationDTO.getAddressId() == null) {
                        return Mono.error(new BusinessException("O doador escolheu 'Prefiro que venham até mim', porém não foi informado o código do endereço."));
                    }

                    var donationEntity = donationMapper.toEntity(createNewDonationDTO);
                    donationEntity.setDonorId(userDTO.getId());
                    donationEntity.setStatus(DonationStatus.PENDENTE);

                    if (!createNewDonationDTO.getIsDelivery()) {
                        return s3Service.uploadF(file)
                                .flatMap(imageSrc -> addressService.findById(createNewDonationDTO.getAddressId())
                                        .flatMap(addressDTO -> {
                                            donationEntity.setImageSrc(imageSrc);
                                            donationEntity.setDonorAddressId(addressDTO.getId());
                                            return donationRepository.save(donationEntity);
                                        })
                                );
                    }

                    return s3Service.uploadF(file)
                            .flatMap(s -> {
                                donationEntity.setImageSrc(s);
                                return donationRepository.save(donationEntity);
                            });
                })
                .flatMap(this::mapToDonationDTO);
    }

    @Override
    public Mono<DonationDTO> update(Integer id, PatchDonationDTO body) {
        return userService.getUser()
                .flatMap(userDTO -> donationRepository.findByIdAndDonorId(id, userDTO.getId()))
                .switchIfEmpty(Mono.error(new BusinessException("Doação não encontrada. Verifique se a doação pertence ao usuário!")))
                .flatMap(donation -> {
                    donation.setType(body.getTypeOfDonation());
                    donation.setDescription(body.getDescription());
                    return donationRepository.save(donation);
                })
                .flatMap(this::mapToDonationDTO);
    }

    @Override
    public Mono<DonationDTO> receive(Integer id, ReceiveDonationDTO receiveDonation) {
        return this.getDonation(id)
                .flatMap(donation -> userService.getUser()
                        .flatMap(user -> {
                            // se o doador escolheu que vai levar até o local e o recebedor não passou o seu endereço então lança exceção
                            if (donation.getIsDelivery() && receiveDonation.getAddressId() == null) {
                                return Mono.error(new BusinessException("O Doador preferiu entregar no local, porém não foi informado o código do endereço."));
                            }

                            donation.setReceiverId(user.getId());
                            donation.setStatus(DonationStatus.FINALIZADO);

                            if (donation.getIsDelivery()) {
                                return addressService.findById(receiveDonation.getAddressId())
                                        .flatMap(addressDTO -> {
                                            donation.setReceiverAddressId(addressDTO.getId());
                                            return donationRepository.save(donation);
                                        });
                            }

                            return donationRepository.save(donation);
                        })
                )
                .flatMap(this::mapToDonationDTO);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return userService.getUser()
                .flatMap(userDTO -> donationRepository.findByIdAndDonorId(id, userDTO.getId()))
                .switchIfEmpty(Mono.error(new BusinessException("Doação não encontrada. Verifique se a doação pertence ao usuário!")))
                .flatMap(donationRepository::delete)
                .onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new BusinessException("Deleção não permitida! A doação está relacionada com outros cadastros.")));
    }

    private Mono<DonationDTO> mapToDonationDTO(Donation donation) {
        return Mono.zip(
                        Mono.just(donation),
                        donation.getDonorId() != null ? userService.getUserById(donation.getDonorId()) : Mono.just(UserDTO.builder().build()),
                        donation.getReceiverId() != null ? userService.getUserById(donation.getReceiverId()) : Mono.just(UserDTO.builder().build()))
                .flatMap(tuple -> {
                    var donationEntity = tuple.getT1();
                    var donor = tuple.getT2();
                    var receiver = tuple.getT3();

                    var donationDTO = donationMapper.toDto(donationEntity);

                    donationDTO.setDonor(DonorReceiverDTO.builder().id(donor.getId()).name(donor.getName()).build());
                    donationDTO.setReceiver(DonorReceiverDTO.builder().id(receiver.getId()).name(receiver.getName()).build());

                    return Mono.zip(
                            Mono.just(donationDTO),
                            donationEntity.getDonorAddressId() != null ? addressService.findById(donationEntity.getDonorAddressId()) : Mono.just(AddressDTO.builder().build()),
                            donationEntity.getReceiverAddressId() != null ? addressService.findById(donationEntity.getReceiverAddressId()) : Mono.just(AddressDTO.builder().build())
                    );
                })
                .map(tuple -> {
                    var donationDTO = tuple.getT1();
                    var donorAddress = tuple.getT2();
                    var receiverAddress = tuple.getT3();

                    if (donationDTO.getReceiver().getId() == null) donationDTO.setReceiver(null);
                    if (donorAddress.getId() != null) donationDTO.getDonor().setAddress(donorAddress);
                    if (receiverAddress.getId() != null) donationDTO.getReceiver().setAddress(receiverAddress);

                    return donationDTO;
                });
    }

    protected Mono<Donation> getDonation(Integer id) {
        return donationRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.DONATION_NOT_FOUND)));
    }

}
