package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "ativo")
@Getter
@Setter
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ativo")
    @SequenceGenerator(name = "seq_ativo", sequenceName = "seq_ativo", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @ManyToOne
    @JoinColumn(name = "tipo_equipamento_id", nullable = false)
    private TipoEquipamento tipoEquipamento;

    @ManyToOne
    @JoinColumn(name = "localizacao_id", nullable = false)
    private Localizacao localizacao;

    @Column(name = "numero_serie", nullable = false, length = 100, unique = true)
    private String numeroSerie;

    @Column(name = "dt_instalacao", nullable = false)
    private LocalDate dtInstalacao;

    @Column(name = "horas_uso_acumuladas")
    private Double horasUsoAcumuladas = 0.0;

    // operacional, em_manutencao ou inativo
    @Column(name = "status", nullable = false, length = 20)
    private String status = "operacional";
}
