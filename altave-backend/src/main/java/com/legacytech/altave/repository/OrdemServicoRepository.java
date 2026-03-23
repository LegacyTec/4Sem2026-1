package com.legacytech.altave.repository;

import com.legacytech.altave.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    // Busca a ordem de servico vinculada a uma manutencao
    Optional<OrdemServico> findByManutencaoId(Long manutencaoId);

    // Ordens de um tecnico especifico
    List<OrdemServico> findByTecnicoId(Long tecnicoId);

    // Ordens abertas (planejada ou em campo)
    List<OrdemServico> findByStatusIn(List<String> statusList);
}
