package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status")
@Getter
@Setter
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hist_status")
    @SequenceGenerator(name = "seq_hist_status", sequenceName = "seq_hist_status", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manutencao_id", nullable = false)
    private Manutencao manutencao;

    @Column(name = "status_anterior", length = 20)
    private String statusAnterior;

    @Column(name = "status_novo", nullable = false, length = 20)
    private String statusNovo;

    @Column(name = "dt_transicao", nullable = false)
    private LocalDateTime dtTransicao;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "motivo", length = 500)
    private String motivo;
}
