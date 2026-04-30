package com.sem2026_1.altave_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository UsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

       @Override
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() ->{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico não encontrado com esse ID!");
        });
    }
}
