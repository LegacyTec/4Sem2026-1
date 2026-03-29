package com.legacytech.altave.controller;

import com.legacytech.altave.model.Tecnico;
import com.legacytech.altave.service.TecnicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
@Tag(name = "Tecnicos", description = "Endpoints para consulta de tecnicos de campo")
public class TecnicoController {

    @Autowired
    private TecnicoService tecnicoService;

    @GetMapping
    @Operation(summary = "Lista todos os tecnicos")
    public ResponseEntity<List<Tecnico>> listarTodos() {
        return ResponseEntity.ok(tecnicoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um tecnico pelo id")
    public ResponseEntity<Tecnico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscarPorId(id));
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Lista apenas os tecnicos com status disponivel")
    public ResponseEntity<List<Tecnico>> listarDisponiveis() {
        return ResponseEntity.ok(tecnicoService.listarDisponiveis());
    }
}
