package com.sem2026_1.altave_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.repository.AtivoRepository;

public class AtivoServiceImpl implements AtivoService{

    AtivoRepository ativoRepository;

    public AtivoServiceImpl(AtivoRepository ativoRepository) {
        this.ativoRepository = ativoRepository;
    }

    @Override 
    public Ativo buscarPorId(Long id){

        return ativoRepository.findById(id).orElseThrow(()->{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum ativo com esse id!");
        });

    }
}
