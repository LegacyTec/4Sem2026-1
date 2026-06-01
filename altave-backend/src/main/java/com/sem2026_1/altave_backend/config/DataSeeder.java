package com.sem2026_1.altave_backend.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.repository.UsuarioRepository;

/**
 * Cria usuários de teste no startup.
 * Idempotente por e-mail; garante senha em usuários já existentes.
 */
@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private static final String SENHA_PADRAO = "12345";

    private final UsuarioRepository usuarioRepository;

    public DataSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        garantirUsuario("Marcos Fernando", "marcos.fernando@altave.com", "ADM", "Administrador", LocalDate.of(1985, 4, 12));
        garantirUsuario("Juliana Rocha", "juliana.rocha@altave.com", "ADM", "Administradora", LocalDate.of(1988, 9, 3));
        garantirUsuario("Carla Souza", "carla.souza@altave.com", "Supervisor", "Supervisão de campo", LocalDate.of(1990, 6, 18));
        garantirUsuario("Fernanda Lima", "fernanda.lima@altave.com", "Supervisor", "Supervisão industrial", LocalDate.of(1987, 11, 25));
        garantirUsuario("Pedro Almeida", "pedro.almeida@altave.com", "Planejador", "Planejamento de manutenção", LocalDate.of(1992, 2, 14));
        garantirUsuario("Lucas Pereira", "lucas.pereira@altave.com", "Planejador", "Planejamento operacional", LocalDate.of(1991, 8, 7));
        garantirUsuario("João Silva", "joao.silva@altave.com", "Técnico", "Manutenção", LocalDate.of(1995, 3, 10));
        garantirUsuario("Ana Costa", "ana.costa@altave.com", "Técnico", "Manutenção elétrica", LocalDate.of(1994, 12, 1));
        garantirUsuario("Ricardo Mendes", "ricardo.mendes@altave.com", "Técnico", "Manutenção mecânica", LocalDate.of(1993, 5, 22));
        garantirUsuario("Bruno Santos", "bruno.santos@altave.com", "Técnico", "Inspeção", LocalDate.of(1996, 7, 30));
    }

    private void garantirUsuario(String nome, String email, String cargo, String funcao, LocalDate nascimento) {
        usuarioRepository.findByEmail(email).ifPresentOrElse(
            existente -> {
                if (existente.getSenha() == null || existente.getSenha().isBlank()) {
                    existente.setSenha(SENHA_PADRAO);
                    usuarioRepository.save(existente);
                }
            },
            () -> {
                Usuario u = new Usuario();
                u.setNomeCompleto(nome);
                u.setEmail(email);
                u.setSenha(SENHA_PADRAO);
                u.setCargo(cargo);
                u.setFuncao(funcao);
                u.setStatus("ATIVO");
                u.setDataNascimento(nascimento);
                usuarioRepository.save(u);
            }
        );
    }
}
