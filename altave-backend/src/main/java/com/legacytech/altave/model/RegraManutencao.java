package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regra_manutencao")
@Getter
@Setter
public class RegraManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_regra_manut")
    @SequenceGenerator(name = "seq_regra_manut", sequenceName = "seq_regra_manut", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    // preventiva ou corretiva
    @Column(name = "tipo_manutencao", nullable = false, length = 20)
    private String tipoManutencao;

    @Column(name = "periodicidade_dias")
    private Integer periodicidadeDias;

    @Column(name = "limite_horas_uso")
    private Double limiteHorasUso;

    @Column(name = "antecedencia_alerta", nullable = false)
    private Integer antecedenciaAlerta = 7;
}
