package com.sem2026_1.altave_backend.service;

import java.util.List;

import com.sem2026_1.altave_backend.dto.AtivoCreateDTO;
import com.sem2026_1.altave_backend.entity.Ativo;

public interface AtivoService {

    List<Ativo> listarPorContrato(Long contratoId);

    Ativo criar(Long contratoId, AtivoCreateDTO dto);
}
