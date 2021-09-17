package com.labes.doe.model.donation;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

    @Column("codigo_usuario")
    private Integer userId;

    @Column("descricao")
    private String description;

}
