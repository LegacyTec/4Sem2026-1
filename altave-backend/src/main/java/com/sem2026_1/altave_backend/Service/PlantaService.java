package com.sem2026_1.altave_backend.service;

import java.util.List;

import com.sem2026_1.altave_backend.dto.PlantaCreateDTO;
import com.sem2026_1.altave_backend.entity.Planta;

public interface PlantaService {

    List<Planta> listarPorContrato(Long contratoId);

    Planta criar(PlantaCreateDTO dto);
}
