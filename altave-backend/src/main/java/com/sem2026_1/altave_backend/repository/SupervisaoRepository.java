package com.sem2026_1.altave_backend.repository;

import com.sem2026_1.altave_backend.entity.Supervisao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupervisaoRepository extends JpaRepository<Supervisao, Long> {
    List<Supervisao> findByContratoId(Long contratoId);
}
