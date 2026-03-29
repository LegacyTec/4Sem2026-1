package com.legacytech.altave.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AtivoResponse {

    private Long id;
    private String numeroSerie;
    private String status;
    private LocalDate dtInstalacao;
    private Double horasUsoAcumuladas;

    private Long contratoId;
    private String numeroContrato;

    private Long tipoEquipamentoId;
    private String nomeEquipamento;

    private Long localizacaoId;
    private String nomeLocalizacaoAtual;
}
