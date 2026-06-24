package com.sem2026_1.altave_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.dto.SupervisaoCreateDTO;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.Planta;
import com.sem2026_1.altave_backend.entity.Supervisao;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.repository.ContratoRepository;
import com.sem2026_1.altave_backend.repository.PlantaRepository;
import com.sem2026_1.altave_backend.repository.SupervisaoRepository;

import jakarta.transaction.Transactional;

@Service
public class SupervisaoServiceImpl implements SupervisaoService {

    private final SupervisaoRepository supervisaoRepository;
    private final ContratoRepository contratoRepository;
    private final UsuarioService usuarioService;
    private final PlantaRepository plantaRepository;

    public SupervisaoServiceImpl(
            SupervisaoRepository supervisaoRepository,
            ContratoRepository contratoRepository,
            UsuarioService usuarioService,
            PlantaRepository plantaRepository) {
        this.supervisaoRepository = supervisaoRepository;
        this.contratoRepository = contratoRepository;
        this.usuarioService = usuarioService;
        this.plantaRepository = plantaRepository;
    }

    @Override
    public List<Supervisao> listarPorContrato(Long contratoId) {
        return supervisaoRepository.findByContratoId(contratoId);
    }

    @Override
    @Transactional
    public Supervisao criar(SupervisaoCreateDTO dto) {
        Contrato contrato = contratoRepository.findById(dto.idContrato())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado"));
        Usuario supervisor = usuarioService.buscarPorId(dto.idSupervisor());
        List<Planta> plantas = plantaRepository.findAllById(dto.idPlantas());

        Supervisao supervisao = new Supervisao();
        supervisao.setNome(dto.nome());
        supervisao.setDescricao(dto.descricao());
        supervisao.setContrato(contrato);
        supervisao.setSupervisor(supervisor);
        supervisao.setPlantas(plantas);

        return supervisaoRepository.save(supervisao);
    }

    @Override
    @Transactional
    public Supervisao atualizarSupervisor(Long supervisaoId, Long idSupervisor) {
        Supervisao supervisao = supervisaoRepository.findById(supervisaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supervisão não encontrada: " + supervisaoId));

        if (idSupervisor != null) {
            Usuario supervisor = usuarioService.buscarPorId(idSupervisor);
            supervisao.setSupervisor(supervisor);
        }

        return supervisaoRepository.save(supervisao);
    }
}
