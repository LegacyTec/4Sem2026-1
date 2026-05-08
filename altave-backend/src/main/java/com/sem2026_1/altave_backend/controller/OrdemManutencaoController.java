package com.sem2026_1.altave_backend.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.OrdemManutencao;
import com.sem2026_1.altave_backend.service.OrdemManutencaoService;

@RestController
@CrossOrigin
@RequestMapping("/ordem")
public class OrdemManutencaoController {

    OrdemManutencaoService ordemManutencaoService;

    public OrdemManutencaoController(OrdemManutencaoService ordemManutencaoService) {
        this.ordemManutencaoService = ordemManutencaoService;
    }

    @GetMapping
    @JsonView(View.Ordem.class)
    public ResponseEntity<List<OrdemManutencao>> listar() {
        return ResponseEntity.ok(ordemManutencaoService.listar());
    }

    @PostMapping
    @JsonView(View.Ordem.class)
    public ResponseEntity<OrdemManutencao> cadastrar(@RequestBody OrdemManutencao ordem) {
        ordem = ordemManutencaoService.cadastrarManutencao(ordem);
        return ResponseEntity.created(URI.create("/ordem/" + ordem.getId())).body(ordem);
    }

    @PatchMapping("/{id}/status")
    @JsonView(View.Ordem.class)
    public ResponseEntity<OrdemManutencao> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String novoStatus = body.get("status");
        if (novoStatus == null || novoStatus.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ordemManutencaoService.atualizarStatus(id, novoStatus));
    }
}
