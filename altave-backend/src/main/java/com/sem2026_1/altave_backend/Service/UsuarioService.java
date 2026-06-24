package com.sem2026_1.altave_backend.service;

import java.util.List;

import com.sem2026_1.altave_backend.entity.Usuario;

public interface UsuarioService {

    Usuario buscarPorId(Long id);

    List<Usuario> listar();

    Usuario criar(Usuario usuario);

    Usuario autenticar(String email, String senha);
}
