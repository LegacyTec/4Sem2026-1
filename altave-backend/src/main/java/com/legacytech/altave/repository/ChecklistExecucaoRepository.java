package com.legacytech.altave.repository;

import com.legacytech.altave.model.ChecklistExecucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChecklistExecucaoRepository extends JpaRepository<ChecklistExecucao, Long> {

    // Todos os itens do checklist de uma ordem de servico
    List<ChecklistExecucao> findByOrdemServicoId(Long ordemServicoId);

    // Verifica se ainda existem itens obrigatorios nao concluidos em uma ordem
    @Query("SELECT COUNT(ce) > 0 FROM ChecklistExecucao ce " +
           "JOIN ce.item ci " +
           "WHERE ce.ordemServico.id = :ordemServicoId " +
           "AND ci.obrigatorio = 'S' " +
           "AND ce.concluido = 'N'")
    boolean existeItemObrigatorioNaoConcluido(@Param("ordemServicoId") Long ordemServicoId);
}
