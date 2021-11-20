package com.labes.doe.model;

import com.labes.doe.model.enumeration.DonationStatus;
import com.labes.doe.model.enumeration.DonationType;
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

    @Column("codigo_endereco_doador")
    private Integer donorAddressId;

    @Column("codigo_endereco_recebedor")
    private Integer receiverAddressId;

    @Column("tipo_doacao")
    private DonationType type;

    @Column("descricao")
    private String description;

    @Column("data_hora")
    private LocalDateTime date;

    @Column("status")
    private DonationStatus status;

    @Column("is_entrega")
    private Boolean isDelivery;

    @Column("email")
    private String email;

    @Column("telefone")
    private String phone;

    @Column("caminho_imagem")
    private String imageSrc;

}
