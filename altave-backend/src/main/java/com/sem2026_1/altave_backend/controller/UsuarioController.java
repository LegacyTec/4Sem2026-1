package com.sem2026_1.altave_backend.controller;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.service.UsuarioService;

@RestController
@CrossOrigin
@RequestMapping("/usuario")
public class UsuarioController {
    
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/contar")
    public Long contarUsuarios(){
        return usuarioService.contagemDeUsuario();
    } 

    @GetMapping
    @JsonView(View.Usuario.class)
    public ResponseEntity<List<Usuario>> listar(){
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{usuario}")
    @JsonView(View.Usuario.class)
    public ResponseEntity<Usuario> buscarPorId(@RequestParam Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}
