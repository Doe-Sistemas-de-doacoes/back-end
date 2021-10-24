package com.labes.doe.model.donation;

import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.model.enumeration.DonationType;
import com.labes.doe.model.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("cadastro_doacao")
public class Donation {

    @Id
    @Column("codigo")
    private Integer id;

    @Column("codigo_doador")
    private Integer donorId;

    @Column("codigo_recebedor")
    private Integer receiverId;

    @Column("descricao")
    private String description;

    @Column("tipo_doacao")
    private DonationType typeOfDonation;

    @Column("busca_em_casa")
    private Boolean isPickUpAtHome;

    @Column("data_hora_coleta_doador")
    private LocalDateTime datetimeOfCollection;

    @Column("data_hora_entrega_recebedor")
    private LocalDateTime datetimeOfDelivery;

    @Column("status_entrega_recebedor")
    private DonationStatus statusDelivery;

    @Column("status_coleta_doador")
    private DonationStatus statusCollection;

    @Transient
    private User donor;

    @Transient
    private User receiver;


}
