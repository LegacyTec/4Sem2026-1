package com.sem2026_1.altave_backend.repository;

import com.sem2026_1.altave_backend.entity.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlantaRepository extends JpaRepository<Planta, Long> {
    List<Planta> findByContratoId(Long contratoId);
}
