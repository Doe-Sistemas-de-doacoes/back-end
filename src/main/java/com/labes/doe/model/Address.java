package com.labes.doe.model;

import com.labes.doe.model.enumeration.Region;
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
    private String number;

    @Column("rua_avenida")
    private String street;

    @Column("regiao")
    private Region region;

    @Column("codigo_usuario")
    private Integer userId;

}
