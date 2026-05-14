package com.sem2026_1.altave_backend.controller;

import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.service.ContratoService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PatchMapping;

import com.sem2026_1.altave_backend.dto.ContratoResumoDTO;

@RestController
@CrossOrigin
@RequestMapping("/contrato")
public class ContratoController {
    
    private ContratoService contratoService;
    
    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @JsonView(View.Contrato.class)
    @GetMapping
    public ResponseEntity<List<Contrato>> listar(){
        return ResponseEntity.ok(contratoService.buscarTodos());
    } 

    @JsonView(View.Contrato.class)
    @PostMapping
    public ResponseEntity<Contrato> cadastrar(@RequestBody Contrato contrato){
        contrato =  contratoService.cadastrarContrato(contrato);
        return ResponseEntity.created(URI.create("/contrato/" + contrato.getId())).body(contrato);
    }
    
    @JsonView(View.Contrato.class)
    @PutMapping("/{id}")
    public ResponseEntity<Contrato> editar(@PathVariable Long id, @RequestBody Contrato contrato){
        contrato =  contratoService.editarContrato(id, contrato);
        return ResponseEntity.ok(contratoService.editarContrato(id, contrato));
    }

    @JsonView(View.Contrato.class)
    @PatchMapping("/{id}/desabilitar")
    public ResponseEntity<Contrato> desabilitar(@PathVariable Long id) {
        return ResponseEntity.ok(contratoService.desabilitarContrato(id));
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<ContratoResumoDTO>> listarResumo() {
        return ResponseEntity.ok(contratoService.buscarContratosComTotalOrdens());
    }
}

