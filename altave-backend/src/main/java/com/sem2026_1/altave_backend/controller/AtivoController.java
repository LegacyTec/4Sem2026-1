package com.sem2026_1.altave_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.Supervisao;
import com.sem2026_1.altave_backend.repository.AtivoRepository;
import com.sem2026_1.altave_backend.repository.ContratoRepository;
import com.sem2026_1.altave_backend.repository.SupervisaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class AtivoController {

    private final AtivoRepository ativoRepository;
    private final ContratoRepository contratoRepository;
    private final SupervisaoRepository supervisaoRepository;

    public AtivoController(AtivoRepository ativoRepository,
                           ContratoRepository contratoRepository,
                           SupervisaoRepository supervisaoRepository) {
        this.ativoRepository = ativoRepository;
        this.contratoRepository = contratoRepository;
        this.supervisaoRepository = supervisaoRepository;
    }

    @GetMapping("/contrato/{id}/ativos")
    @JsonView(View.Ordem.class)
    public ResponseEntity<List<Ativo>> listarPorContrato(@PathVariable Long id) {
        return ResponseEntity.ok(ativoRepository.findByContratoId(id));
    }

    // Payload DTO para receber a criação de ativo com campo idSupervisao
    record AtivoPayload(
        String nome,
        String tipo,
        String status,
        String fabricante,
        Long periodicidadeManutencao,
        String descricao,
        String dataInstalacao,
        String predio,
        String planta,
        Long idSupervisao
    ) {}

    /**
     * POST /contrato/{id}/ativos
     *
     * Cria o ativo vinculando-o ao contrato (pelo id da URL) e,
     * opcionalmente, a uma supervisão (pelo idSupervisao no body).
     */
    @PostMapping("/contrato/{id}/ativos")
    @JsonView(View.Ordem.class)
    public ResponseEntity<Ativo> criar(@PathVariable Long id,
                                       @RequestBody AtivoPayload payload) {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contrato não encontrado: " + id));

        Ativo ativo = new Ativo();
        ativo.setNome(payload.nome());
        ativo.setTipo(payload.tipo() != null ? payload.tipo().toUpperCase() : null);
        ativo.setStatus(payload.status());
        ativo.setFabricante(payload.fabricante());
        ativo.setPeriodicidadeManutencao(payload.periodicidadeManutencao());
        ativo.setDescricao(payload.descricao());
        if (payload.dataInstalacao() != null && !payload.dataInstalacao().isBlank()) {
            ativo.setDataInstalacao(LocalDate.parse(payload.dataInstalacao()));
        }
        ativo.setPredio(payload.predio());
        ativo.setPlanta(payload.planta());
        ativo.setContrato(contrato);

        if (payload.idSupervisao() != null) {
            Supervisao supervisao = supervisaoRepository.findById(payload.idSupervisao())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Supervisão não encontrada: " + payload.idSupervisao()));
            ativo.setSupervisao(supervisao);
        }

        Ativo salvo = ativoRepository.save(ativo);
        contratoRepository.incrementarQuantidadeAtivos(id);
        return ResponseEntity.ok(salvo);
    }
}
