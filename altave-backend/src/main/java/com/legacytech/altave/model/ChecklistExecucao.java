package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_execucao")
@Getter
@Setter
public class ChecklistExecucao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ck_execucao")
    @SequenceGenerator(name = "seq_ck_execucao", sequenceName = "seq_ck_execucao", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ChecklistItem item;

    // S ou N
    @Column(name = "concluido", nullable = false, length = 1)
    private String concluido = "N";

    @Column(name = "observacao", columnDefinition = "CLOB")
    private String observacao;

    @Column(name = "dt_registro", nullable = false)
    private LocalDateTime dtRegistro;
}
