package com.sem2026_1.altave_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sem2026_1.altave_backend.dto.ContratoResumoDTO;
import com.sem2026_1.altave_backend.entity.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query("SELECT new com.sem2026_1.altave_backend.dto.ContratoResumoDTO(" +
           "c.id, c.nomeEmpresa, c.status, COUNT(o), c.dataInicio, c.dataFim, c.quantidadePlanta, c.quantidadeAtivos) " +
           "FROM Contrato c " +
           "LEFT JOIN c.ativos a " +
           "LEFT JOIN a.ordens o " +
           "GROUP BY c.id, c.nomeEmpresa, c.status, c.dataInicio, c.dataFim, c.quantidadePlanta, c.quantidadeAtivos")
    List<ContratoResumoDTO> buscarContratosComTotalOrdens();
}