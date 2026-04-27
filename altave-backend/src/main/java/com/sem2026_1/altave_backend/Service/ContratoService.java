package com.sem2026_1.altave_backend.Service;

import java.util.List;

import com.sem2026_1.altave_backend.entity.Contrato;

public interface ContratoService {

    public List<Contrato> buscarTodos();

    public Contrato cadastrarContrato(Contrato contrato);
}

