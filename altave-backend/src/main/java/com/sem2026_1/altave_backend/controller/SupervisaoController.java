package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.dto.SupervisaoCreateDTO;
import com.sem2026_1.altave_backend.entity.Supervisao;
import com.sem2026_1.altave_backend.service.SupervisaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class SupervisaoController {

    private final SupervisaoService supervisaoService;

    public SupervisaoController(SupervisaoService supervisaoService) {
        this.supervisaoService = supervisaoService;
    }

    @GetMapping("/contrato/{id}/supervisoes")
    @JsonView(View.Contrato.class)
    public ResponseEntity<List<Supervisao>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(supervisaoService.listarPorContrato(id));
    }

    @PatchMapping("/supervisao/{id}/supervisor")
    @JsonView(View.Contrato.class)
    public ResponseEntity<Supervisao> atualizarSupervisor(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(supervisaoService.atualizarSupervisor(id, body.get("idSupervisor")));
    }

    @PostMapping("/supervisao")
    @JsonView(View.Contrato.class)
    public ResponseEntity<Supervisao> criar(@RequestBody SupervisaoCreateDTO payload) {
        return ResponseEntity.ok(supervisaoService.criar(payload));
    }
}
