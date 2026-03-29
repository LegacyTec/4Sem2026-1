package com.legacytech.altave.controller;

import com.legacytech.altave.model.Ativo;
import com.legacytech.altave.service.AtivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ativos")
@Tag(name = "Ativos", description = "Endpoints para consulta de equipamentos")
public class AtivoController {

    @Autowired
    private AtivoService ativoService;

    @GetMapping
    @Operation(summary = "Lista todos os ativos")
    public ResponseEntity<List<Ativo>> listarTodos() {
        return ResponseEntity.ok(ativoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um ativo pelo id")
    public ResponseEntity<Ativo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ativoService.buscarPorId(id));
    }

    @GetMapping("/contrato/{contratoId}")
    @Operation(summary = "Lista os ativos de um contrato")
    public ResponseEntity<List<Ativo>> listarPorContrato(@PathVariable Long contratoId) {
        return ResponseEntity.ok(ativoService.listarPorContrato(contratoId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Lista ativos por status (operacional, em_manutencao ou inativo)")
    public ResponseEntity<List<Ativo>> listarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(ativoService.listarPorStatus(status));
    }
}
