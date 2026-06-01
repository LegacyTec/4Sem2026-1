package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.Planta;
import com.sem2026_1.altave_backend.repository.ContratoRepository;
import com.sem2026_1.altave_backend.repository.PlantaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class PlantaController {

    private final PlantaRepository plantaRepository;
    private final ContratoRepository contratoRepository;

    public PlantaController(PlantaRepository plantaRepository, ContratoRepository contratoRepository) {
        this.plantaRepository = plantaRepository;
        this.contratoRepository = contratoRepository;
    }

    @GetMapping("/contrato/{id}/plantas")
    @JsonView(View.Contrato.class)
    public ResponseEntity<List<Planta>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(plantaRepository.findByContratoId(id));
    }

    @PostMapping("/planta")
    @JsonView(View.Contrato.class)
    public ResponseEntity<Planta> criar(@RequestBody Map<String, Object> body) {
        String nome = (String) body.get("nome");
        Long idContrato = Long.valueOf(body.get("idContrato").toString());
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado: " + idContrato));
        Planta planta = new Planta();
        planta.setNome(nome);
        planta.setContrato(contrato);
        if (body.get("latitude") != null) {
            planta.setLatitude(Double.parseDouble(body.get("latitude").toString()));
        }
        if (body.get("longitude") != null) {
            planta.setLongitude(Double.parseDouble(body.get("longitude").toString()));
        }
        return ResponseEntity.ok(plantaRepository.save(planta));
    }
}
