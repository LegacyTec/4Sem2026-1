package com.legacytech.altave.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ManutencaoRequest {

    private Long ativoId;

    // preventiva ou corretiva
    private String tipo;

    // alta, media ou baixa
    private String criticidade;

    private LocalDate dtPrevista;

    private LocalDate dtLimite;

    private String observacoes;
}
