package com.legacytech.altave.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class TecnicoCertificacaoId implements Serializable {

    @Column(name = "tecnico_id")
    private Long tecnicoId;

    @Column(name = "certificacao_id")
    private Long certificacaoId;
}
