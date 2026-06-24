package com.sem2026_1.altave_backend.service;

import java.util.List;

import com.sem2026_1.altave_backend.dto.SupervisaoCreateDTO;
import com.sem2026_1.altave_backend.entity.Supervisao;

public interface SupervisaoService {

    List<Supervisao> listarPorContrato(Long contratoId);

    Supervisao criar(SupervisaoCreateDTO dto);

    Supervisao atualizarSupervisor(Long supervisaoId, Long idSupervisor);
}
