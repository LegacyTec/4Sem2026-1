package com.legacytech.altave.repository;

import com.legacytech.altave.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AtivoRepository extends JpaRepository<Ativo, Long> {

    // Busca ativos por contrato
    List<Ativo> findByContratoId(Long contratoId);

    // Busca ativos por status
    List<Ativo> findByStatus(String status);

    // Busca ativos operacionais de um contrato vigente (usados para gerar preventivas)
    @Query("SELECT a FROM Ativo a " +
           "WHERE a.status = 'operacional' " +
           "AND a.contrato.dtFim >= CURRENT_DATE")
    List<Ativo> buscarOperacionaisComContratoVigente();

    // Busca por numero de serie
    Ativo findByNumeroSerie(String numeroSerie);

    // Ativos por tipo de equipamento
    List<Ativo> findByTipoEquipamentoId(Long tipoEquipamentoId);
}
