package com.sem2026_1.altave_backend.service;

import java.util.List;

import com.sem2026_1.altave_backend.entity.OrdemManutencao;

public interface OrdemManutencaoService {
    
    public List<OrdemManutencao> listar();

    public OrdemManutencao cadastrarManutencao(OrdemManutencao ordem);

    public Long contarPorStatus();
}
