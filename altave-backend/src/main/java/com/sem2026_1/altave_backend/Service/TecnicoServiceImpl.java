package com.sem2026_1.altave_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.entity.Tecnico;
import com.sem2026_1.altave_backend.repository.TecnicoRepository;

@Service
public class TecnicoServiceImpl implements TecnicoService{

    private TecnicoRepository tecnicoRepository;

    public TecnicoServiceImpl(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

       @Override
    public Tecnico buscarPorId(Long id){
        return tecnicoRepository.findById(id).orElseThrow(() ->{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico não encontrado com esse ID!");
        });
    }
}
