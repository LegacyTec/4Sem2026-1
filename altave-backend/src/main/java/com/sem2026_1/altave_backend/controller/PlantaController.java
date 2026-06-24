package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.dto.PlantaCreateDTO;
import com.sem2026_1.altave_backend.entity.Planta;
import com.sem2026_1.altave_backend.service.PlantaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class PlantaController {

    private final PlantaService plantaService;

    public PlantaController(PlantaService plantaService) {
        this.plantaService = plantaService;
    }

    @GetMapping("/contrato/{id}/plantas")
    @JsonView(View.Contrato.class)
    public ResponseEntity<List<Planta>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(plantaService.listarPorContrato(id));
    }

    @PostMapping("/planta")
    @JsonView(View.Contrato.class)
    public ResponseEntity<Planta> criar(@RequestBody PlantaCreateDTO dto) {
        return ResponseEntity.ok(plantaService.criar(dto));
    }
}
