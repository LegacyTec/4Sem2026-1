package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "contrato")
@Getter
@Setter
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contrato")
    @SequenceGenerator(name = "seq_contrato", sequenceName = "seq_contrato", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "numero", nullable = false, length = 50, unique = true)
    private String numero;

    @Column(name = "criticidade", nullable = false, length = 10)
    private String criticidade;

    @Column(name = "sla_dias", nullable = false)
    private Integer slaDias;

    @Column(name = "dt_inicio", nullable = false)
    private LocalDate dtInicio;

    @Column(name = "dt_fim", nullable = false)
    private LocalDate dtFim;
}
