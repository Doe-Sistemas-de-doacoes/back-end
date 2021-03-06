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
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final UserService userService;
    private final AddressService addressService;
    private final S3Service s3Service;

    @Override
    public Flux<DonationDTO> findAll(DonationStatus status) {
        return getIdLogedUser()
                .flatMapMany(userId -> donationRepository
                        .findByStatusAndDonorIdIsNot(status, userId) )
                .switchIfEmpty( donationRepository.findByStatus(status) )
                .flatMap(this::mapToDonationDTO);
    }

    @Override
    public Mono<Long> count( DonationStatus status ) {
        return donationRepository.countByStatus(status);
    }

    @Override
    public Flux<DonationDTO> findByUser() {
        return userService.getUser()
                .flatMapMany(userDTO -> donationRepository.findAllByDonorIdOrReceiverId(userDTO.getId(), userDTO.getId()))
                .flatMap(this::mapToDonationDTO);
    }


    @Override
    public Mono<DonationDTO> save(Mono<FilePart> file, CreateNewDonationDTO createNewDonationDTO) {
        return userService.getUser()
                .flatMap(userDTO -> {
                    // se o doador escolheu que vai levar at?? o local e o recebedor n??o passou o seu endere??o ent??o lan??a a exce????o
                    if ( !createNewDonationDTO.getIsDelivery() && createNewDonationDTO.getAddressId() == null ){
                        return Mono.error( new BusinessException("O doador escolheu 'Prefiro que venham at?? mim', por??m n??o foi informado o c??digo do endere??o.") );
                    }

                    var donationEntity = donationMapper.toEntity(createNewDonationDTO);
                    donationEntity.setDonorId(userDTO.getId());
                    donationEntity.setStatus(DonationStatus.PENDENTE);

                    if ( !createNewDonationDTO.getIsDelivery() ){
                        return s3Service.uploadF(file)
                                .flatMap( imageSrc -> addressService.findById(createNewDonationDTO.getAddressId() )
                                    .flatMap( addressDTO -> {
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
                .flatMap( this::mapToDonationDTO );
    }

    @Override
    public Mono<DonationDTO> update(Integer id, PatchDonationDTO body) {
        return userService.getUser()
                .flatMap(userDTO -> donationRepository.findByIdAndDonorId(id,userDTO.getId()))
                .switchIfEmpty(Mono.error(new BusinessException("Doa????o n??o encontrada. Verifique se a doa????o pertence ao usu??rio!")))
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
                            // se o doador escolheu que vai levar at?? o local e o recebedor n??o passou o seu endere??o ent??o lan??a exce????o
                            if ( donation.getIsDelivery() && receiveDonation.getAddressId() == null ){
                                return Mono.error( new BusinessException("O Doador preferiu entregar no local, por??m n??o foi informado o c??digo do endere??o.") );
                            }

                            donation.setReceiverId( user.getId() );
                            donation.setStatus(DonationStatus.FINALIZADO);

                            if ( donation.getIsDelivery() ){
                                return addressService.findById( receiveDonation.getAddressId() )
                                        .flatMap( addressDTO -> {
                                            donation.setReceiverAddressId( addressDTO.getId() );
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
                .flatMap(userDTO -> donationRepository.findByIdAndDonorId(id,userDTO.getId()))
                .switchIfEmpty(Mono.error(new BusinessException("Doa????o n??o encontrada. Verifique se a doa????o pertence ao usu??rio!")))
                .flatMap(donationRepository::delete)
                .onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new BusinessException("Dele????o n??o permitida! A doa????o est?? relacionada com outros cadastros.")));
    }

    private Mono<DonationDTO> mapToDonationDTO( Donation donation ) {
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
                            Mono.just( donationDTO ),
                            donationEntity.getDonorAddressId() != null ? addressService.findById( donationEntity.getDonorAddressId() ) : Mono.just( AddressDTO.builder().build() ),
                            donationEntity.getReceiverAddressId() != null ? addressService.findById( donationEntity.getReceiverAddressId() ) : Mono.just( AddressDTO.builder().build() )
                    );
                })
                .map(tuple -> {
                    var donationDTO = tuple.getT1();
                    var donorAddress = tuple.getT2();
                    var receiverAddress = tuple.getT3();

                    if( donationDTO.getReceiver().getId() == null ) donationDTO.setReceiver(null);
                    if( donorAddress.getId() != null ) donationDTO.getDonor().setAddress(donorAddress);
                    if( receiverAddress.getId() != null ) donationDTO.getReceiver().setAddress(receiverAddress);

                    return donationDTO;
                });
    }

    protected Mono<Donation> getDonation(Integer id) {
        return donationRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(MessageUtil.DONATION_NOT_FOUND)));
    }

    private Mono<Integer> getIdLogedUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    var principal = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
                    return principal.getId();
                });
    }

}
