package com.legacytech.altave.service;

import com.legacytech.altave.model.Ativo;
import com.legacytech.altave.repository.AtivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AtivoService {

    @Autowired
    private AtivoRepository ativoRepository;

    public List<Ativo> listarTodos() {
        return ativoRepository.findAll();
    }

    public Ativo buscarPorId(Long id) {
        return ativoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ativo nao encontrado"));
    }

    public List<Ativo> listarPorContrato(Long contratoId) {
        return ativoRepository.findByContratoId(contratoId);
    }

    public List<Ativo> listarPorStatus(String status) {
        return ativoRepository.findByStatus(status);
    }
}
