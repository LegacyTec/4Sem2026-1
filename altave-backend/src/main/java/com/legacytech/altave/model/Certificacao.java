package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "certificacao")
@Getter
@Setter
public class Certificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_certificacao")
    @SequenceGenerator(name = "seq_certificacao", sequenceName = "seq_certificacao", allocationSize = 1)
    private Long id;

    @Column(name = "nome", nullable = false, length = 50, unique = true)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "validade_meses", nullable = false)
    private Integer validadeMeses;
}
