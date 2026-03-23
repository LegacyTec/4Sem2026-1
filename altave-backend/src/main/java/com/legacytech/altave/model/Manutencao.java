package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "manutencao")
@Getter
@Setter
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manutencao")
    @SequenceGenerator(name = "seq_manutencao", sequenceName = "seq_manutencao", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    // preventiva ou corretiva
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    // pendente, alocada, em_execucao, concluida, cancelada ou vencida
    @Column(name = "status", nullable = false, length = 20)
    private String status = "pendente";

    // alta, media ou baixa
    @Column(name = "criticidade", nullable = false, length = 10)
    private String criticidade;

    @Column(name = "dt_prevista", nullable = false)
    private LocalDate dtPrevista;

    @Column(name = "dt_limite", nullable = false)
    private LocalDate dtLimite;

    @Column(name = "dt_inicio_execucao")
    private LocalDateTime dtInicioExecucao;

    @Column(name = "dt_conclusao")
    private LocalDateTime dtConclusao;

    @Column(name = "horas_uso_no_momento")
    private Double horasUsoNoMomento = 0.0;

    @Column(name = "observacoes", columnDefinition = "CLOB")
    private String observacoes;

    @Column(name = "gerado_automaticamente", nullable = false, length = 1)
    private String geradoAutomaticamente = "N";
}
