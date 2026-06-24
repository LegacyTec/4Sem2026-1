package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.dto.AtivoCreateDTO;
import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.service.AtivoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AtivoController {

    private final AtivoService ativoService;

    public AtivoController(AtivoService ativoService) {
        this.ativoService = ativoService;
    }

    @GetMapping("/contrato/{id}/ativos")
    @JsonView(View.Ordem.class)
    public ResponseEntity<List<Ativo>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(ativoService.listarPorContrato(id));
    }

    @PostMapping("/contrato/{id}/ativos")
    @JsonView(View.Ordem.class)
    public ResponseEntity<Ativo> criar(@PathVariable Long id,
                                       @RequestBody AtivoCreateDTO payload) {
        return ResponseEntity.ok(ativoService.criar(id, payload));
    }
}
