package com.legacytech.altave.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TecnicoResponse {
    
    private Long id;
    private String nome;
    private String email;
    private String nivel;
    private String status;
    private String nomeLocalizacaoAtual;
    private String paisLocalizacaoAtual;
    
}
