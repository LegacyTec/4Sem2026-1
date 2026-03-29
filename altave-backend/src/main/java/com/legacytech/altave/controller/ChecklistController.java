package com.legacytech.altave.controller;

import com.legacytech.altave.dto.ChecklistExecucaoRequest;
import com.legacytech.altave.model.ChecklistExecucao;
import com.legacytech.altave.service.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens")
@Tag(name = "Checklist", description = "Endpoints para execucao do checklist de campo")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @GetMapping("/{ordemServicoId}/checklist")
    @Operation(summary = "Lista todos os itens do checklist de uma ordem de servico")
    public ResponseEntity<List<ChecklistExecucao>> listar(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(checklistService.listarPorOrdem(ordemServicoId));
    }

    @PatchMapping("/checklist/{itemId}")
    @Operation(summary = "Marca um item do checklist como concluido e registra observacao")
    public ResponseEntity<ChecklistExecucao> atualizar(
            @PathVariable Long itemId,
            @RequestBody ChecklistExecucaoRequest request) {
        return ResponseEntity.ok(checklistService.atualizarItem(itemId, request));
    }

    @GetMapping("/{ordemServicoId}/checklist/validar")
    @Operation(summary = "Verifica se todos os itens obrigatorios do checklist foram concluidos")
    public ResponseEntity<Boolean> validar(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(checklistService.todosObrigatoriosConcluidos(ordemServicoId));
    }
}
