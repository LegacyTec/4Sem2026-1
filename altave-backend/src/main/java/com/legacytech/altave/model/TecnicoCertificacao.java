package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tecnico_certificacao")
@Getter
@Setter
public class TecnicoCertificacao {

    @EmbeddedId
    private TecnicoCertificacaoId id;

    @ManyToOne
    @MapsId("tecnicoId")
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @ManyToOne
    @MapsId("certificacaoId")
    @JoinColumn(name = "certificacao_id")
    private Certificacao certificacao;

    @Column(name = "dt_obtencao", nullable = false)
    private LocalDate dtObtencao;

    @Column(name = "dt_vencimento", nullable = false)
    private LocalDate dtVencimento;

    @Column(name = "documento_ref", length = 200)
    private String documentoRef;
}
