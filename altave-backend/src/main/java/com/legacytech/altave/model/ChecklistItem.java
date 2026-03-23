package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "checklist_item")
@Getter
@Setter
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ck_item")
    @SequenceGenerator(name = "seq_ck_item", sequenceName = "seq_ck_item", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private ChecklistTemplate template;

    @Column(name = "descricao", nullable = false, length = 500)
    private String descricao;

    // S ou N
    @Column(name = "obrigatorio", nullable = false, length = 1)
    private String obrigatorio = "N";
}
