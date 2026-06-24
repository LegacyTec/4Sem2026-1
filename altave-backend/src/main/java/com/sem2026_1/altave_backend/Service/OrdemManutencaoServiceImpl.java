package com.sem2026_1.altave_backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.entity.OrdemManutencao;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.entity.enums.StatusOrdem;
import com.sem2026_1.altave_backend.repository.OrdemManutencaoRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdemManutencaoServiceImpl implements OrdemManutencaoService {

    OrdemManutencaoRepository ordemManutencaoRepository;

    UsuarioService usuarioService;

    public OrdemManutencaoServiceImpl(OrdemManutencaoRepository ordemManutencaoRepository, UsuarioService usuarioService) {
        this.ordemManutencaoRepository = ordemManutencaoRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<OrdemManutencao> listar(){
       return ordemManutencaoRepository.findAll();
    }

    @Transactional
    @Override
    public OrdemManutencao cadastrarManutencao(OrdemManutencao ordemManutencao){
        if(ordemManutencao.getId() != null ||
            ordemManutencao.getNome().isBlank() ||
            ordemManutencao.getNome() == null ||
            ordemManutencao.getDataInicio() == null ||
            ordemManutencao.getTipoManutencao() == null ||
            ordemManutencao.getAtivo() == null ||
            ordemManutencao.getAtivo().getId() == null ||
            ordemManutencao.getStatus() == null
    ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos para cadastrar a ordem de manutenção!");
        }
        if (ordemManutencao.getUsuarios() != null) {
            Set<Usuario> usuarios = new HashSet<>();

            for(Usuario usr: ordemManutencao.getUsuarios()){
                usuarios.add(usuarioService.buscarPorId(usr.getId()));
            }
            ordemManutencao.setUsuarios(usuarios);
        }
        return ordemManutencaoRepository.save(ordemManutencao);
    }

    @Override
    @Transactional
    public OrdemManutencao atualizarStatus(Long id, String novoStatus) {
        if (novoStatus == null || novoStatus.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status é obrigatório");
        }

        OrdemManutencao ordem = ordemManutencaoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordem não encontrada!"));
        try {
            ordem.setStatus(StatusOrdem.valueOf(novoStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + novoStatus);
        }
        return ordemManutencaoRepository.save(ordem);
    }

}
