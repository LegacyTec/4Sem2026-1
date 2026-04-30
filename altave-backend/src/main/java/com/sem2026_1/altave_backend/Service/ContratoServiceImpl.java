package com.sem2026_1.altave_backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.repository.ContratoRepository;

@Service
public class ContratoServiceImpl implements ContratoService  {

    private ContratoRepository contratoRepository;
    private UsuarioService usuarioService;

    public ContratoServiceImpl(ContratoRepository contratoRepository, UsuarioService usuarioService) {
        this.contratoRepository = contratoRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<Contrato> buscarTodos(){
        return contratoRepository.findAll();
    }

    @Override
    public Contrato cadastrarContrato(Contrato contrato){

        if(
            contrato == null ||
            contrato.getId() != null ||
            contrato.getNomeEmpresa().isBlank() ||
            contrato.getNomeEmpresa() == null ||
            contrato.getQuantidadePlanta() == null ||
            contrato.getDataInicio() == null || 
            contrato.getDataFim() == null || 
            contrato.getDescricao() == null ||
            contrato.getDescricao().isBlank() ||
            contrato.getUsuarios().isEmpty() ||
            contrato.getUsuarios() == null
        ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dados inválidos para cadastrar o contrato!");
        }
        Set<Usuario> tecnicos = new HashSet<>();

        for(Usuario tecnico: contrato.getUsuarios()){
            tecnicos.add(usuarioService.buscarPorId(tecnico.getId()));
        }
        contrato.setUsuarios(tecnicos);

        return contratoRepository.save(contrato);
    }
    
}
