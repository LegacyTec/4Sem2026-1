package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ordem_servico")
    @SequenceGenerator(name = "seq_ordem_servico", sequenceName = "seq_ordem_servico", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manutencao_id", nullable = false)
    private Manutencao manutencao;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Tecnico tecnico;

    // planejada, em_campo ou encerrada
    @Column(name = "status", nullable = false, length = 20)
    private String status = "planejada";

    @Column(name = "dt_saida")
    private LocalDate dtSaida;

    @Column(name = "dt_retorno")
    private LocalDate dtRetorno;

    @Column(name = "tempo_viagem_h")
    private Double tempoViagemH = 0.0;
}
