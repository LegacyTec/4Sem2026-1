package com.legacytech.altave.repository;

import com.legacytech.altave.model.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {

    // Todo o historico de transicoes de uma manutencao, do mais recente ao mais antigo
    List<HistoricoStatus> findByManutencaoIdOrderByDtTransicaoDesc(Long manutencaoId);
}
