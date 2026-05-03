package com.sem2026_1.altave_backend.service;

import java.util.List;

import com.sem2026_1.altave_backend.entity.Usuario;

public interface UsuarioService {
    
    public Usuario buscarPorId(Long id);

    public Long contagemDeUsuario();

    public List<Usuario> listar();
}
