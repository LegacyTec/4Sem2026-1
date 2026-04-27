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

@RestController
@CrossOrigin
@RequestMapping("/contrato")
public class ContratoController {
    
    private ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    @JsonView(View.Contrato.class)
    public ResponseEntity<List<Contrato>> listar(){
        return ResponseEntity.ok(contratoService.buscarTodos());
    } 

    @PostMapping
    @JsonView(View.Contrato.class)
    public ResponseEntity<Contrato> cadastrar(@RequestBody Contrato contrato){
        contrato =  contratoService.cadastrarContrato(contrato);
        return ResponseEntity.created(URI.create("/contrato/" + contrato.getId())).body(contrato);
    }   
}
