package com.sem2026_1.altave_backend.dto;

public record PlantaCreateDTO(
        String nome,
        Long idContrato,
        Double latitude,
        Double longitude
) {}
