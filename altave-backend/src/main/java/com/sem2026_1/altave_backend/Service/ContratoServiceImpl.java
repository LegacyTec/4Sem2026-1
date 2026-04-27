package com.sem2026_1.altave_backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.Tecnico;
import com.sem2026_1.altave_backend.repository.ContratoRepository;

@Service
public class ContratoServiceImpl implements ContratoService  {

    private ContratoRepository contratoRepository;
    private TecnicoService tecnicoService;

    public ContratoServiceImpl(ContratoRepository contratoRepository, TecnicoService tecnicoService) {
        this.contratoRepository = contratoRepository;
        this.tecnicoService = tecnicoService;
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
            contrato.getTecnicos().isEmpty() ||
            contrato.getTecnicos() == null
        ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dados inválidos para cadastrar o contrato!");
        }
        Set<Tecnico> tecnicos = new HashSet<>();

        for(Tecnico tecnico: contrato.getTecnicos()){
            tecnicos.add(tecnicoService.buscarPorId(tecnico.getId()));
        }
        contrato.setTecnicos(tecnicos);

        return contratoRepository.save(contrato);
    }
    
}
