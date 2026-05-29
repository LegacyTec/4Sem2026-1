package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.repository.AtivoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AtivoController {

    private final AtivoRepository ativoRepository;

    public AtivoController(AtivoRepository ativoRepository) {
        this.ativoRepository = ativoRepository;
    }

    @GetMapping("/contrato/{id}/ativos")
    @JsonView(View.Ordem.class)
    public ResponseEntity<List<Ativo>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(ativoRepository.findByContratoId(id));
    }

    @PostMapping("/ativo")
    @JsonView(View.Ordem.class)
    public ResponseEntity<Ativo> criar(@RequestBody Ativo ativo) {
        ativo.setId(null); // garante que é criação, não atualização
        return ResponseEntity.ok(ativoRepository.save(ativo));
    }
}
