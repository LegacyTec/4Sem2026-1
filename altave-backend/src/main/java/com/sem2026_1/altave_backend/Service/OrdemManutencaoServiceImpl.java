package com.sem2026_1.altave_backend.service;

import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.entity.OrdemManutencao;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.entity.enums.StatusOrdem;
import com.sem2026_1.altave_backend.repository.OrdemManutencaoRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdemManutencaoServiceImpl implements OrdemManutencaoService {
    
    OrdemManutencaoRepository ordemManutencaoRepository;

    UsuarioService usuarioService;

    AtivoService AtivoService;

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
            ordemManutencao.getTipoOrdem() == null ||
            ordemManutencao.getDataFim() == null ||
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
        if(ordemManutencao.getDataInicio() == null){
            ordemManutencao.setDataInicio(LocalDate.now());
        }

        Ativo ativo = ordemManutencao.getAtivo();
        AtivoService.buscarPorId(ativo.getId());

        return ordemManutencaoRepository.save(ordemManutencao);
    }

    
    public Long contarPorStatus(){
        return ordemManutencaoRepository.countByStatus(StatusOrdem.EM_ANDAMENTO);
    }

    public Long contarPorStatusPendente(){
        return ordemManutencaoRepository.contarPorStatusPendente(StatusOrdem.PENDENTE);
    }

    public Long contarOrdens(){
        return ordemManutencaoRepository.count();
    }

}
