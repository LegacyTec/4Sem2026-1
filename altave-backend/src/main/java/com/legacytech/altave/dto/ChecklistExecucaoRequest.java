package com.legacytech.altave.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChecklistExecucaoRequest {

    // S ou N
    private String concluido;

    private String observacao;
}
