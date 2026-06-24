package com.sem2026_1.altave_backend.dto;

import java.util.List;

public record SupervisaoCreateDTO(
        String nome,
        String descricao,
        Long idContrato,
        Long idSupervisor,
        List<Long> idPlantas
) {}
