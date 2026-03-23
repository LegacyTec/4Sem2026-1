package com.legacytech.altave.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ManutencaoResponse {

    private Long id;
    private Long ativoId;
    private String numeroSerieAtivo;
    private Long tecnicoId;
    private String nomeTecnico;
    private String tipo;
    private String status;
    private String criticidade;
    private LocalDate dtPrevista;
    private LocalDate dtLimite;
    private LocalDateTime dtInicioExecucao;
    private LocalDateTime dtConclusao;
    private Double horasUsoNoMomento;
    private String observacoes;
    private String geradoAutomaticamente;
}
