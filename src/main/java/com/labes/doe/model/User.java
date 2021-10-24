package com.labes.doe.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("cadastro_usuario")
public class User {
    @Id
    @Column("codigo")
    private Integer id;

    @Column("usuario")
    private String user;

    @Column("senha")
    private String password;

    @Column("nome")
    private String name;

    @Column("perfil")
    private Integer profile;

}
