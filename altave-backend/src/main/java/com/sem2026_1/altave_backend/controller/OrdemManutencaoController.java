package com.sem2026_1.altave_backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

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
    public ResponseEntity<List<OrdemManutencao>> listar(){
       return ResponseEntity.ok(ordemManutencaoService.listar());
    }

    @PostMapping
    @JsonView(View.Ordem.class)
    public ResponseEntity<OrdemManutencao> cadastrar(@RequestBody OrdemManutencao ordem){
        ordem = ordemManutencaoService.cadastrarManutencao(ordem);

        return ResponseEntity.created(URI.create("/ordem/" + ordem.getId())).body(ordem);
    }

    @GetMapping("/andamento")
    public Long contarPorStatus(){
        return ordemManutencaoService.contarPorStatus();
    }

    @GetMapping("/pendente")
    public Long contarPorStatusPendente(){
        return ordemManutencaoService.contarPorStatusPendente();
    }

    @GetMapping("/ordens-totais")
    public Long contarOrdens(){
        return ordemManutencaoService.contarOrdens();
    }
}
