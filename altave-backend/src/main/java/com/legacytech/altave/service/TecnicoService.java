package com.legacytech.altave.service;

import com.legacytech.altave.model.Tecnico;
import com.legacytech.altave.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public List<Tecnico> listarTodos() {
        return tecnicoRepository.findAll();
    }

    public Tecnico buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tecnico nao encontrado"));
    }

    public List<Tecnico> listarDisponiveis() {
        return tecnicoRepository.findByStatus("disponivel");
    }
}
