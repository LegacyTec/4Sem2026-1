package com.sem2026_1.altave_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.repository.UsuarioRepository;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    record LoginRequest(String email, String senha) {}

    /**
     * POST /auth/login
     *
     * Autenticação simples (projeto acadêmico, sem Spring Security).
     * Compara email + senha em texto puro com o que está no banco.
     * Retorna o usuário (sem a senha, que é @JsonIgnore) ou 401.
     */
    @PostMapping("/login")
    @JsonView(View.Contrato.class)
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest req) {
        if (req == null || req.email() == null || req.senha() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Usuario usuario = usuarioRepository.findByEmail(req.email().trim()).orElse(null);
        if (usuario == null
                || usuario.getSenha() == null
                || !usuario.getSenha().equals(req.senha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usuario);
    }
}
