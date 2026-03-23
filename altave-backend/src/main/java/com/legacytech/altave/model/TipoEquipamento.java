package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_equipamento")
@Getter
@Setter
public class TipoEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipo_equip")
    @SequenceGenerator(name = "seq_tipo_equip", sequenceName = "seq_tipo_equip", allocationSize = 1)
    private Long id;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "fabricante", length = 200)
    private String fabricante;

    @Column(name = "modelo", length = 200)
    private String modelo;
}
