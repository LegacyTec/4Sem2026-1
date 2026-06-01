package com.sem2026_1.altave_backend.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sem2026_1.altave_backend.entity.Ativo;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.OrdemManutencao;
import com.sem2026_1.altave_backend.entity.Planta;
import com.sem2026_1.altave_backend.entity.Supervisao;
import com.sem2026_1.altave_backend.entity.Usuario;
import com.sem2026_1.altave_backend.entity.enums.StatusOrdem;
import com.sem2026_1.altave_backend.entity.enums.TipoOrdem;
import com.sem2026_1.altave_backend.repository.AtivoRepository;
import com.sem2026_1.altave_backend.repository.ContratoRepository;
import com.sem2026_1.altave_backend.repository.OrdemManutencaoRepository;
import com.sem2026_1.altave_backend.repository.PlantaRepository;
import com.sem2026_1.altave_backend.repository.SupervisaoRepository;
import com.sem2026_1.altave_backend.repository.UsuarioRepository;

/**
 * Popula contratos, plantas, supervisões, ativos e ordens de demonstração.
 * Idempotente: não recria se o contrato "PETROBRAS SJK" já existir.
 */
@Component
@Order(2)
public class DemoDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);
    private static final String MARCA_DEMO = "PETROBRAS SJK";

    private final ContratoRepository contratoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlantaRepository plantaRepository;
    private final SupervisaoRepository supervisaoRepository;
    private final AtivoRepository ativoRepository;
    private final OrdemManutencaoRepository ordemRepository;

    public DemoDataSeeder(
            ContratoRepository contratoRepository,
            UsuarioRepository usuarioRepository,
            PlantaRepository plantaRepository,
            SupervisaoRepository supervisaoRepository,
            AtivoRepository ativoRepository,
            OrdemManutencaoRepository ordemRepository) {
        this.contratoRepository = contratoRepository;
        this.usuarioRepository = usuarioRepository;
        this.plantaRepository = plantaRepository;
        this.supervisaoRepository = supervisaoRepository;
        this.ativoRepository = ativoRepository;
        this.ordemRepository = ordemRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (contratoRepository.findByNomeEmpresa(MARCA_DEMO).isPresent()) {
            log.info("Dados demo já existem ({}). Pulando seed.", MARCA_DEMO);
            return;
        }

        log.info("Populando banco com dados de demonstração...");

        Usuario carla = user("carla.souza@altave.com");
        Usuario fernanda = user("fernanda.lima@altave.com");
        Usuario pedro = user("pedro.almeida@altave.com");
        Usuario lucas = user("lucas.pereira@altave.com");
        Usuario joao = user("joao.silva@altave.com");
        Usuario ana = user("ana.costa@altave.com");
        Usuario ricardo = user("ricardo.mendes@altave.com");
        Usuario bruno = user("bruno.santos@altave.com");
        Usuario marcos = user("marcos.fernando@altave.com");

        seedPetrobras(carla, fernanda, pedro, joao, ana, ricardo, bruno, marcos);
        seedVale(fernanda, lucas, joao, ana);
        seedBraskem(carla, pedro, ricardo);

        log.info("Seed demo concluído com sucesso.");
    }

    private void seedPetrobras(
            Usuario carla, Usuario fernanda, Usuario pedro,
            Usuario joao, Usuario ana, Usuario ricardo, Usuario bruno, Usuario marcos) {

        Contrato contrato = criarContrato(
                MARCA_DEMO,
                3,
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2027, 3, 1),
                "Manutenção de câmeras, sensores e geradores nas plantas SJK, BOT e ITA",
                "Marcos Fernando",
                2,
                Set.of(carla, fernanda, pedro, joao, ana, ricardo, bruno, marcos)
        );

        Planta sjk = criarPlanta("SJK", contrato, -23.2237, -45.9009);
        Planta bot = criarPlanta("BOT", contrato, -1.4558, -48.5034);
        Planta ita = criarPlanta("ITA", contrato, -22.9099, -47.0626);

        Supervisao supNorte = criarSupervisao("Supervisão Norte", "Plantas SJK e BOT", contrato, carla, List.of(sjk, bot));
        Supervisao supSul = criarSupervisao("Supervisão Sul", "Planta ITA", contrato, fernanda, List.of(ita));

        Ativo cam1 = criarAtivo("Câmera PTZ Entrada", "CAMERA", "OPERACIONAL", "Intelbras", 30,
                "Câmera PTZ entrada principal", LocalDate.of(2024, 4, 10), "Prédio A", "SJK", contrato, supNorte);
        Ativo sen1 = criarAtivo("Sensor Pressão A-01", "SENSOR", "OPERACIONAL", "Honeywell", 45,
                "Sensor de pressão linha A", LocalDate.of(2024, 5, 2), "Prédio B", "SJK", contrato, supNorte);
        Ativo ger1 = criarAtivo("Gerador Diesel G1", "GERADOR", "EM_MANUTENCAO", "Cummins", 60,
                "Gerador backup principal", LocalDate.of(2023, 11, 15), "Casa de Máquinas", "SJK", contrato, supNorte);
        Ativo cam2 = criarAtivo("Câmera LPR Estacionamento", "CAMERA", "OPERACIONAL", "Hikvision", 30,
                "Leitura de placas estacionamento", LocalDate.of(2024, 6, 20), "Estacionamento", "SJK", contrato, supNorte);
        Ativo sen2 = criarAtivo("Sensor Temperatura T-12", "SENSOR", "OPERACIONAL", "Siemens", 15,
                "Monitoramento sala servidores", LocalDate.of(2024, 8, 1), "Prédio C", "SJK", contrato, supNorte);
        Ativo nob1 = criarAtivo("Nobreak N-03", "NOBREAK", "INATIVO", "APC", 90,
                "Nobreak sala controle", LocalDate.of(2022, 3, 5), "Prédio A", "SJK", contrato, supNorte);

        Ativo cam3 = criarAtivo("Câmera Móvel BOT-01", "CAMERA", "OPERACIONAL", "Intelbras", 30,
                "Câmera móvel área portuária", LocalDate.of(2024, 7, 12), "Píer 2", "BOT", contrato, supNorte);
        Ativo sen3 = criarAtivo("Sensor Vibração V-08", "SENSOR", "EM_MANUTENCAO", "SKF", 20,
                "Vibração compressor", LocalDate.of(2024, 2, 28), "Compressoria", "BOT", contrato, supNorte);
        Ativo cam4 = criarAtivo("Câmera Fixa BOT-02", "CAMERA", "OPERACIONAL", "Hikvision", 30,
                "Vigilância perímetro norte", LocalDate.of(2024, 9, 5), "Perímetro", "BOT", contrato, supNorte);

        Ativo cam5 = criarAtivo("Câmera Fixa ITA-01", "CAMERA", "OPERACIONAL", "Intelbras", 30,
                "Entrada administrativa ITA", LocalDate.of(2024, 10, 1), "Recepção", "ITA", contrato, supSul);
        Ativo sen4 = criarAtivo("Sensor CO2 S-04", "SENSOR", "OPERACIONAL", "Honeywell", 15,
                "Qualidade do ar laboratório", LocalDate.of(2024, 10, 15), "Lab 3", "ITA", contrato, supSul);
        Ativo ger2 = criarAtivo("Gerador ITA-G2", "GERADOR", "OPERACIONAL", "Cummins", 60,
                "Gerador secundário ITA", LocalDate.of(2023, 12, 10), "Subestação", "ITA", contrato, supSul);
        Ativo sen5 = criarAtivo("Sensor Umidade H-02", "SENSOR", "OPERACIONAL", "Siemens", 15,
                "Umidade depósito", LocalDate.of(2025, 1, 8), "Depósito", "ITA", contrato, supSul);
        Ativo cam6 = criarAtivo("Câmera Dome ITA-02", "CAMERA", "OPERACIONAL", "Hikvision", 30,
                "Corredor produção", LocalDate.of(2025, 2, 1), "Produção", "ITA", contrato, supSul);

        // Ordens concluídas (MTTR / MTBF)
        criarOrdem("PM Câmera PTZ Q1", TipoOrdem.PREVENTIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 1, 5), LocalDate.of(2025, 1, 7), cam1, joao, "Preventiva trimestral");
        criarOrdem("PM Câmera PTZ Q2", TipoOrdem.PREVENTIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 4, 5), LocalDate.of(2025, 4, 6), cam1, ana, "Limpeza e calibração");
        criarOrdem("Corretiva PTZ foco", TipoOrdem.CORRETIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 11), cam1, joao, "Ajuste de foco");
        criarOrdem("Corretiva PTZ cabo", TipoOrdem.CORRETIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 5, 20), LocalDate.of(2025, 5, 22), cam1, ricardo, "Substituição cabo rede");

        criarOrdem("PM Sensor Pressão", TipoOrdem.PREVENTIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 2), sen1, ana, "Calibração");
        criarOrdem("Corretiva Sensor drift", TipoOrdem.CORRETIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 1, 15), LocalDate.of(2025, 1, 16), sen1, bruno, "Drift leitura");

        criarOrdem("PM Gerador mensal", TipoOrdem.PREVENTIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 3), ger1, ricardo, "Teste carga");
        criarOrdem("Corretiva Gerador G1", TipoOrdem.CORRETIVA, StatusOrdem.EM_ANDAMENTO,
                LocalDate.of(2025, 5, 25), LocalDate.of(2025, 5, 28), ger1, joao, "Troca filtro combustível");

        criarOrdem("PM LPR semestral", TipoOrdem.PREVENTIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 2), cam2, bruno, "Atualização firmware");

        criarOrdem("Inspeção Sensor T-12", TipoOrdem.PREVENTIVA, StatusOrdem.PENDENTE,
                LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 3), sen2, ana, "Verificação sensores");

        criarOrdem("Corretiva Vibração V-08", TipoOrdem.CORRETIVA, StatusOrdem.EM_ANDAMENTO,
                LocalDate.of(2025, 5, 20), LocalDate.of(2025, 5, 27), sen3, ricardo, "Rolamento desgastado");

        criarOrdem("PM BOT Câmera móvel", TipoOrdem.PREVENTIVA, StatusOrdem.PENDENTE,
                LocalDate.of(2025, 6, 5), LocalDate.of(2025, 6, 7), cam3, joao, "Manutenção preventiva");

        criarOrdem("Corretiva CO2 alarme", TipoOrdem.CORRETIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 11), sen4, ana, "Falso alarme");
        criarOrdem("Corretiva CO2 sensor", TipoOrdem.CORRETIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 4, 22), LocalDate.of(2025, 4, 24), sen4, bruno, "Troca célula sensor");

        criarOrdem("PM ITA Gerador", TipoOrdem.PREVENTIVA, StatusOrdem.PENDENTE,
                LocalDate.of(2025, 6, 10), LocalDate.of(2025, 6, 12), ger2, ricardo, "Preventiva semestral");

        criarOrdem("Ordem atrasada demo", TipoOrdem.PREVENTIVA, StatusOrdem.PENDENTE,
                LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 15), sen5, joao, "PM atrasada para KPI");

        atualizarQuantidadeAtivos(contrato);
    }

    private void seedVale(Usuario fernanda, Usuario lucas, Usuario joao, Usuario ana) {
        Contrato contrato = criarContrato(
                "VALE ITABIRA",
                2,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2026, 12, 31),
                "Inspeção e manutenção de sensores e câmeras em mineração",
                "Marcos Fernando",
                1,
                Set.of(fernanda, lucas, joao, ana)
        );

        Planta itabira = criarPlanta("ITABIRA", contrato, -19.6192, -43.2272);
        Planta brumadinho = criarPlanta("BRUMADINHO", contrato, -20.1435, -44.2008);

        Supervisao sup = criarSupervisao("Supervisão Mineração", "Itabira e Brumadinho", contrato, fernanda, List.of(itabira, brumadinho));

        Ativo a1 = criarAtivo("Câmera Torre ITB-01", "CAMERA", "OPERACIONAL", "Hikvision", 30,
                "Torre monitoramento", LocalDate.of(2023, 6, 1), "Torre 1", "ITABIRA", contrato, sup);
        Ativo a2 = criarAtivo("Sensor Nível NV-03", "SENSOR", "OPERACIONAL", "Emerson", 20,
                "Nível silo", LocalDate.of(2023, 8, 15), "Silo 3", "ITABIRA", contrato, sup);
        Ativo a3 = criarAtivo("Câmera BRU-01", "CAMERA", "EM_MANUTENCAO", "Intelbras", 30,
                "Perímetro barragem", LocalDate.of(2024, 1, 20), "Barragem", "BRUMADINHO", contrato, sup);
        Ativo a4 = criarAtivo("Gerador BRU-G1", "GERADOR", "OPERACIONAL", "Cummins", 45,
                "Gerador operacional", LocalDate.of(2023, 11, 1), "Casa força", "BRUMADINHO", contrato, sup);

        criarOrdem("PM Torre ITB", TipoOrdem.PREVENTIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 3), a1, joao, "Preventiva");
        criarOrdem("Corretiva NV-03", TipoOrdem.CORRETIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 3, 5), LocalDate.of(2025, 3, 6), a2, ana, "Calibração");
        criarOrdem("Corretiva BRU câmera", TipoOrdem.CORRETIVA, StatusOrdem.EM_ANDAMENTO,
                LocalDate.of(2025, 5, 18), LocalDate.of(2025, 5, 25), a3, joao, "Lente embaçada");

        atualizarQuantidadeAtivos(contrato);
    }

    private void seedBraskem(Usuario carla, Usuario pedro, Usuario ricardo) {
        Contrato contrato = criarContrato(
                "BRASKEM CAMAÇARI",
                2,
                LocalDate.of(2022, 6, 1),
                LocalDate.of(2025, 8, 31),
                "Contrato de inspeção petroquímica — expira em breve",
                "Marcos Fernando",
                1,
                Set.of(carla, pedro, ricardo)
        );

        Planta cam = criarPlanta("CAMAÇARI", contrato, -12.6975, -38.3236);
        Planta cama = criarPlanta("CAMACARI-2", contrato, -12.7010, -38.3180);

        Supervisao sup = criarSupervisao("Supervisão Petroquímica", "Complexo Camaçari", contrato, carla, List.of(cam, cama));

        Ativo a1 = criarAtivo("Câmera Reator R-01", "CAMERA", "OPERACIONAL", "Bosch", 30,
                "Monitoramento reator", LocalDate.of(2022, 9, 1), "Reator 1", "CAMAÇARI", contrato, sup);
        Ativo a2 = criarAtivo("Sensor Pressão P-22", "SENSOR", "OPERACIONAL", "Honeywell", 15,
                "Pressão linha", LocalDate.of(2023, 1, 10), "Linha 22", "CAMAÇARI", contrato, sup);
        Ativo a3 = criarAtivo("Câmera CAM2-03", "CAMERA", "INATIVO", "Intelbras", 30,
                "Desativada reforma", LocalDate.of(2021, 5, 1), "Galpão 3", "CAMACARI-2", contrato, sup);

        criarOrdem("PM Reator R-01", TipoOrdem.PREVENTIVA, StatusOrdem.CONCLUIDA,
                LocalDate.of(2025, 1, 10), LocalDate.of(2025, 1, 12), a1, ricardo, "Inspeção");
        criarOrdem("Corretiva P-22", TipoOrdem.CORRETIVA, StatusOrdem.PENDENTE,
                LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 5), a2, ricardo, "Vazamento detectado");

        atualizarQuantidadeAtivos(contrato);
    }

    private Usuario user(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + email));
    }

    private Contrato criarContrato(
            String nome,
            int plantas,
            LocalDate inicio,
            LocalDate fim,
            String descricao,
            String criador,
            int qtdSupervisoes,
            Set<Usuario> usuarios) {
        Contrato c = new Contrato();
        c.setNomeEmpresa(nome);
        c.setQuantidadePlanta(plantas);
        c.setDataInicio(inicio);
        c.setDataFim(fim);
        c.setDescricao(descricao);
        c.setCriador(criador);
        c.setQuantidadeSupervisao(qtdSupervisoes);
        c.setStatus("ATIVO");
        c.setQuantidadeAtivos(0);
        c.setUsuarios(new HashSet<>(usuarios));
        return contratoRepository.save(c);
    }

    private Planta criarPlanta(String nome, Contrato contrato, double lat, double lng) {
        Planta p = new Planta();
        p.setNome(nome);
        p.setContrato(contrato);
        p.setLatitude(lat);
        p.setLongitude(lng);
        return plantaRepository.save(p);
    }

    private Supervisao criarSupervisao(
            String nome,
            String descricao,
            Contrato contrato,
            Usuario supervisor,
            List<Planta> plantas) {
        Supervisao s = new Supervisao();
        s.setNome(nome);
        s.setDescricao(descricao);
        s.setContrato(contrato);
        s.setSupervisor(supervisor);
        s.setPlantas(plantas);
        return supervisaoRepository.save(s);
    }

    private Ativo criarAtivo(
            String nome,
            String tipo,
            String status,
            String fabricante,
            long periodicidade,
            String descricao,
            LocalDate instalacao,
            String predio,
            String planta,
            Contrato contrato,
            Supervisao supervisao) {
        Ativo a = new Ativo();
        a.setNome(nome);
        a.setTipo(tipo);
        a.setStatus(status);
        a.setFabricante(fabricante);
        a.setPeriodicidadeManutencao(periodicidade);
        a.setDescricao(descricao);
        a.setDataInstalacao(instalacao);
        a.setPredio(predio);
        a.setPlanta(planta);
        a.setContrato(contrato);
        a.setSupervisao(supervisao);
        return ativoRepository.save(a);
    }

    private void criarOrdem(
            String nome,
            TipoOrdem tipo,
            StatusOrdem status,
            LocalDate inicio,
            LocalDate fim,
            Ativo ativo,
            Usuario tecnico,
            String descricao) {
        OrdemManutencao o = new OrdemManutencao();
        o.setNome(nome);
        o.setTipoManutencao(tipo);
        o.setStatus(status);
        o.setDataInicio(inicio);
        o.setDataFim(fim);
        o.setAtivo(ativo);
        o.setDescricao(descricao);
        o.setUsuarios(Set.of(tecnico));
        ordemRepository.save(o);
    }

    private void atualizarQuantidadeAtivos(Contrato contrato) {
        int total = ativoRepository.findByContratoId(contrato.getId()).size();
        contrato.setQuantidadeAtivos(total);
        contratoRepository.save(contrato);
    }
}
