package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "checklist_template")
@Getter
@Setter
public class ChecklistTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ck_template")
    @SequenceGenerator(name = "seq_ck_template", sequenceName = "seq_ck_template", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_equipamento_id", nullable = false)
    private TipoEquipamento tipoEquipamento;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;
}
