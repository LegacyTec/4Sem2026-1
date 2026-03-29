package com.legacytech.altave.controller;

import com.legacytech.altave.dto.AlterarStatusRequest;
import com.legacytech.altave.dto.ManutencaoRequest;
import com.legacytech.altave.dto.ManutencaoResponse;
import com.legacytech.altave.service.ManutencaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manutencoes")
@Tag(name = "Manutencoes", description = "Endpoints para gestao do ciclo de vida das manutencoes")
public class ManutencaoController {

    @Autowired
    private ManutencaoService manutencaoService;

    @PostMapping
    @Operation(summary = "Registra uma nova manutencao corretiva")
    public ResponseEntity<ManutencaoResponse> criar(@RequestBody ManutencaoRequest request) {
        ManutencaoResponse response = manutencaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/fila")
    @Operation(summary = "Lista a fila de manutencoes pendentes ordenada por prioridade")
    public ResponseEntity<List<ManutencaoResponse>> listarFila() {
        return ResponseEntity.ok(manutencaoService.listarFila());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma manutencao pelo id")
    public ResponseEntity<ManutencaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(manutencaoService.buscarPorId(id));
    }

    @GetMapping("/historico/ativo/{ativoId}")
    @Operation(summary = "Retorna o historico de manutencoes concluidas de um ativo")
    public ResponseEntity<List<ManutencaoResponse>> historicoPorAtivo(@PathVariable Long ativoId) {
        return ResponseEntity.ok(manutencaoService.listarPorAtivo(ativoId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Altera o status de uma manutencao (ex: pendente -> alocada -> em_execucao -> concluida)")
    public ResponseEntity<ManutencaoResponse> alterarStatus(
            @PathVariable Long id,
            @RequestBody AlterarStatusRequest request) {
        return ResponseEntity.ok(manutencaoService.alterarStatus(id, request));
    }
}
