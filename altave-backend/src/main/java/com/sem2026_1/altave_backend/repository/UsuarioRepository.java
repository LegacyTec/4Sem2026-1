package com.sem2026_1.altave_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sem2026_1.altave_backend.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
