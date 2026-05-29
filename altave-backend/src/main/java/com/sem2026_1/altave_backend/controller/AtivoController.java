package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.repository.AtivoRepository;
import com.sem2026_1.altave_backend.repository.ContratoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
public class AtivoController {

    private final AtivoRepository ativoRepository;
    private final ContratoRepository contratoRepository;

    public AtivoController(AtivoRepository ativoRepository, ContratoRepository contratoRepository) {
        this.ativoRepository = ativoRepository;
        this.contratoRepository = contratoRepository;
    }

    @GetMapping("/contrato/{id}/ativos")
    @JsonView(View.Ordem.class)
    public ResponseEntity<List<Ativo>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(ativoRepository.findByContratoId(id));
    }

    /**
     * POST /contrato/{id}/ativos
     * Cria um ativo vinculado ao contrato e incrementa quantidade_ativos no contrato.
     * O contrato é carregado do banco pelo id da URL — @JsonIgnore no campo contrato
     * da entidade Ativo impede passá-lo no body.
     */
    @PostMapping("/contrato/{id}/ativos")
    @JsonView(View.Ordem.class)
    public ResponseEntity<Ativo> criar(@PathVariable Long id, @RequestBody Ativo ativo) {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contrato não encontrado: " + id));

        ativo.setId(null);
        ativo.setContrato(contrato);

        Ativo salvo = ativoRepository.save(ativo);

        // mantém o contador quantidade_ativos sincronizado
        int qtdAtual = contrato.getQuantidadeAtivos() == null ? 0 : contrato.getQuantidadeAtivos();
        contrato.setQuantidadeAtivos(qtdAtual + 1);
        contratoRepository.save(contrato);

        return ResponseEntity.ok(salvo);
    }
}
