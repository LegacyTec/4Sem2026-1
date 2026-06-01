package com.sem2026_1.altave_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import com.sem2026_1.altave_backend.dto.ContratoResumoDTO;
import com.sem2026_1.altave_backend.entity.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    Optional<Contrato> findByNomeEmpresa(String nomeEmpresa);

    @Query("SELECT new com.sem2026_1.altave_backend.dto.ContratoResumoDTO(" +
           "c.id, c.nomeEmpresa, c.status, COUNT(o), c.dataInicio, c.dataFim, c.quantidadePlanta, c.quantidadeAtivos) " +
           "FROM Contrato c " +
           "LEFT JOIN c.ativos a " +
           "LEFT JOIN a.ordens o " +
           "GROUP BY c.id, c.nomeEmpresa, c.status, c.dataInicio, c.dataFim, c.quantidadePlanta, c.quantidadeAtivos")
    List<ContratoResumoDTO> buscarContratosComTotalOrdens();

    /** Incrementa quantidade_ativos sem carregar toda a entidade (evita cascade em coleções lazy) */
    @Modifying
    @Transactional
    @Query("UPDATE Contrato c SET c.quantidadeAtivos = COALESCE(c.quantidadeAtivos, 0) + 1 WHERE c.id = :id")
    void incrementarQuantidadeAtivos(@Param("id") Long id);
}
