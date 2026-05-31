package com.sem2026_1.altave_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.repository.UsuarioRepository;

/**
 * Cria usuários de teste no primeiro startup.
 * É idempotente: só cria se ainda não existir um usuário com aquele e-mail,
 * portanto pode rodar a cada deploy sem duplicar dados.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        criarSeNaoExiste("Marcos Fernando", "marcos.fernando@altave.com", "12345", "ADM", "Administrador");
        criarSeNaoExiste("Carla Souza", "carla.souza@altave.com", "12345", "Supervisor", "Supervisão de campo");
        criarSeNaoExiste("Pedro Almeida", "pedro.almeida@altave.com", "12345", "Planejador", "Planejamento de manutenção");
        criarSeNaoExiste("João Silva", "joao.silva@altave.com", "12345", "Técnico", "Manutenção");
    }

    private void criarSeNaoExiste(String nome, String email, String senha, String cargo, String funcao) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return;
        }
        Usuario u = new Usuario();
        u.setNomeCompleto(nome);
        u.setEmail(email);
        u.setSenha(senha);
        u.setCargo(cargo);
        u.setFuncao(funcao);
        u.setStatus("ATIVO");
        usuarioRepository.save(u);
    }
}
