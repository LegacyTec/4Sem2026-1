package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.*;
import com.sem2026_1.altave_backend.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class SupervisaoController {

    private final SupervisaoRepository supervisaoRepository;
    private final ContratoRepository contratoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlantaRepository plantaRepository;

    public SupervisaoController(
            SupervisaoRepository supervisaoRepository,
            ContratoRepository contratoRepository,
            UsuarioRepository usuarioRepository,
            PlantaRepository plantaRepository) {
        this.supervisaoRepository = supervisaoRepository;
        this.contratoRepository = contratoRepository;
        this.usuarioRepository = usuarioRepository;
        this.plantaRepository = plantaRepository;
    }

    @GetMapping("/contrato/{id}/supervisoes")
    @JsonView(View.Contrato.class)
    public ResponseEntity<List<Supervisao>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(supervisaoRepository.findByContratoId(id));
    }

    // DTO interno para receber o payload
    record SupervisaoPayload(
        String nome,
        String descricao,
        Long idContrato,
        Long idSupervisor,
        List<Long> idPlantas
    ) {}

    @PostMapping("/supervisao")
    @JsonView(View.Contrato.class)
    public ResponseEntity<Supervisao> criar(@RequestBody SupervisaoPayload payload) {
        Contrato contrato = contratoRepository.findById(payload.idContrato())
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        Usuario supervisor = usuarioRepository.findById(payload.idSupervisor())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        List<Planta> plantas = plantaRepository.findAllById(payload.idPlantas());

        Supervisao supervisao = new Supervisao();
        supervisao.setNome(payload.nome());
        supervisao.setDescricao(payload.descricao());
        supervisao.setContrato(contrato);
        supervisao.setSupervisor(supervisor);
        supervisao.setPlantas(plantas);

        return ResponseEntity.ok(supervisaoRepository.save(supervisao));
    }
}
