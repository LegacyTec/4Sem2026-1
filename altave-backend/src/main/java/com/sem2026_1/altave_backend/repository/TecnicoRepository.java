package com.sem2026_1.altave_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sem2026_1.altave_backend.entity.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    
}
