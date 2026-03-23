package com.legacytech.altave.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarStatusRequest {

    private String novoStatus;

    // Obrigatorio apenas quando o novo status for "cancelada"
    private String motivo;

    // Opcional: id do usuario que realizou a transicao
    private Long usuarioId;
}
