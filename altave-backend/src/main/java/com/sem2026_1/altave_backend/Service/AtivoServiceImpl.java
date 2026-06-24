package com.sem2026_1.altave_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.dto.AtivoCreateDTO;
import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.Supervisao;
import com.sem2026_1.altave_backend.repository.AtivoRepository;
import com.sem2026_1.altave_backend.repository.ContratoRepository;
import com.sem2026_1.altave_backend.repository.SupervisaoRepository;

import jakarta.transaction.Transactional;

@Service
public class AtivoServiceImpl implements AtivoService {

    private final AtivoRepository ativoRepository;
    private final ContratoRepository contratoRepository;
    private final SupervisaoRepository supervisaoRepository;

    public AtivoServiceImpl(AtivoRepository ativoRepository,
                            ContratoRepository contratoRepository,
                            SupervisaoRepository supervisaoRepository) {
        this.ativoRepository = ativoRepository;
        this.contratoRepository = contratoRepository;
        this.supervisaoRepository = supervisaoRepository;
    }

    @Override
    public List<Ativo> listarPorContrato(Long contratoId) {
        return ativoRepository.findByContratoId(contratoId);
    }

    @Override
    @Transactional
    public Ativo criar(Long contratoId, AtivoCreateDTO dto) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contrato não encontrado: " + contratoId));

        Ativo ativo = new Ativo();
        ativo.setNome(dto.nome());
        ativo.setTipo(dto.tipo() != null ? dto.tipo().toUpperCase() : null);
        ativo.setStatus(dto.status());
        ativo.setFabricante(dto.fabricante());
        ativo.setPeriodicidadeManutencao(dto.periodicidadeManutencao());
        ativo.setDescricao(dto.descricao());
        if (dto.dataInstalacao() != null && !dto.dataInstalacao().isBlank()) {
            ativo.setDataInstalacao(LocalDate.parse(dto.dataInstalacao()));
        }
        ativo.setPredio(dto.predio());
        ativo.setPlanta(dto.planta());
        ativo.setContrato(contrato);

        if (dto.idSupervisao() != null) {
            Supervisao supervisao = supervisaoRepository.findById(dto.idSupervisao())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Supervisão não encontrada: " + dto.idSupervisao()));
            ativo.setSupervisao(supervisao);
        }

        Ativo salvo = ativoRepository.save(ativo);
        contratoRepository.incrementarQuantidadeAtivos(contratoId);
        return salvo;
    }
}
