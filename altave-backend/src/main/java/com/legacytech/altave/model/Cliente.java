package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cliente")
    @SequenceGenerator(name = "seq_cliente", sequenceName = "seq_cliente", allocationSize = 1)
    private Long id;

    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "criticidade", nullable = false, length = 10)
    private String criticidade;

    @Column(name = "ativo", nullable = false, length = 1)
    private String ativo = "S";
}
