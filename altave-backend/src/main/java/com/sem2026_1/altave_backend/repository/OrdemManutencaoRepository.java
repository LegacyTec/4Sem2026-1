package com.sem2026_1.altave_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sem2026_1.altave_backend.entity.OrdemManutencao;
import com.sem2026_1.altave_backend.entity.enums.StatusOrdem;

public interface OrdemManutencaoRepository extends JpaRepository<OrdemManutencao, Long> {
    

        @Query("SELECT COUNT(o) FROM OrdemManutencao o WHERE o.status = :status")
        public Long countByStatus(@Param("status") StatusOrdem status);

        @Query("SELECT COUNT(o) FROM OrdemManutencao o WHERE o.status = :status")
        public Long contarPorStatusPendente(@Param("status") StatusOrdem status);
}
