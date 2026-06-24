package com.sem2026_1.altave_backend.dto;

public record AtivoCreateDTO(
        String nome,
        String tipo,
        String status,
        String fabricante,
        Long periodicidadeManutencao,
        String descricao,
        String dataInstalacao,
        String predio,
        String planta,
        Long idSupervisao
) {}
