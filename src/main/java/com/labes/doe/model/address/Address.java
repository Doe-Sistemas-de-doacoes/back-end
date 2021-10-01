package com.labes.doe.model.address;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("cadastro_endereco")
public class Address {

    @Id
    @Column("codigo")
    private Integer id;

    @Column("bairro")
    private String neighborhood;

    @Column("cidade")
    private String city;

    @Column("estado")
    private String state;

    @Column("numero")
    private Integer number;

    @Column("rua_avenida")
    private String street;

    @Column("codigo_regiao")
    private Integer regionId;

}
