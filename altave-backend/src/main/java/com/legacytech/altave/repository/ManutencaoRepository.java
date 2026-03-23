package com.legacytech.altave.repository;

import com.legacytech.altave.model.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    // Busca todas as manutencoes de um ativo
    List<Manutencao> findByAtivoId(Long ativoId);

    // Busca por status
    List<Manutencao> findByStatus(String status);

    // Busca por tipo e status (ex: preventivas pendentes)
    List<Manutencao> findByTipoAndStatus(String tipo, String status);

    // Verifica se ja existe manutencao ativa para um ativo (evita duplicatas)
    @Query("SELECT COUNT(m) > 0 FROM Manutencao m " +
           "WHERE m.ativo.id = :ativoId " +
           "AND m.status IN ('pendente', 'alocada', 'em_execucao')")
    boolean existeManutencaoAtiva(@Param("ativoId") Long ativoId);

    // Lista a fila de pendentes ordenada por criticidade e prazo
    @Query("SELECT m FROM Manutencao m " +
           "WHERE m.status = 'pendente' " +
           "ORDER BY " +
           "CASE m.criticidade WHEN 'alta' THEN 1 WHEN 'media' THEN 2 ELSE 3 END, " +
           "m.dtLimite ASC")
    List<Manutencao> listarFilaPriorizada();

    // Busca manutencoes com prazo vencido que ainda nao foram marcadas como vencidas
    @Query("SELECT m FROM Manutencao m " +
           "WHERE m.dtLimite < :hoje " +
           "AND m.status NOT IN ('concluida', 'cancelada', 'vencida')")
    List<Manutencao> buscarVencidas(@Param("hoje") LocalDate hoje);

    // Historico completo de um ativo (apenas concluidas)
    @Query("SELECT m FROM Manutencao m " +
           "WHERE m.ativo.id = :ativoId " +
           "AND m.status = 'concluida' " +
           "ORDER BY m.dtConclusao DESC")
    List<Manutencao> buscarHistoricoDoAtivo(@Param("ativoId") Long ativoId);

    // Busca por tecnico alocado
    List<Manutencao> findByTecnicoId(Long tecnicoId);
}
