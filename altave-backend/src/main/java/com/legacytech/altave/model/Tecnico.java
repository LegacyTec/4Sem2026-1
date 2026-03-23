package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tecnico")
@Getter
@Setter
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tecnico")
    @SequenceGenerator(name = "seq_tecnico", sequenceName = "seq_tecnico", allocationSize = 1)
    private Long id;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;

    // junior, pleno ou senior
    @Column(name = "nivel", nullable = false, length = 10)
    private String nivel;

    // disponivel, em_campo, embarcado ou de_folga
    @Column(name = "status", nullable = false, length = 20)
    private String status = "disponivel";

    @ManyToOne
    @JoinColumn(name = "localizacao_base_id")
    private Localizacao localizacaoBase;

    @ManyToOne
    @JoinColumn(name = "localizacao_atual_id")
    private Localizacao localizacaoAtual;

    @Column(name = "lat_atual", precision = 10, scale = 7)
    private Double latAtual;

    @Column(name = "lng_atual", precision = 10, scale = 7)
    private Double lngAtual;

    @Column(name = "dt_ultimo_embarque")
    private LocalDate dtUltimoEmbarque;

    @Column(name = "dt_previsto_retorno")
    private LocalDate dtPrevistoRetorno;
}
