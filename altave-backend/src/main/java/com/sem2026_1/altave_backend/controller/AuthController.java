package com.sem2026_1.altave_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.service.UsuarioService;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    record LoginRequest(String email, String senha) {}

    @PostMapping("/login")
    @JsonView(View.Contrato.class)
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(usuarioService.autenticar(req.email(), req.senha()));
    }
}
