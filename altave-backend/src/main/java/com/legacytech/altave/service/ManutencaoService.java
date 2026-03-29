package com.legacytech.altave.service;

import com.legacytech.altave.dto.AlterarStatusRequest;
import com.legacytech.altave.dto.ManutencaoRequest;
import com.legacytech.altave.dto.ManutencaoResponse;
import com.legacytech.altave.model.Ativo;
import com.legacytech.altave.model.HistoricoStatus;
import com.legacytech.altave.model.Manutencao;
import com.legacytech.altave.repository.AtivoRepository;
import com.legacytech.altave.repository.HistoricoStatusRepository;
import com.legacytech.altave.repository.ManutencaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private AtivoRepository ativoRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Transactional
    public ManutencaoResponse criar(ManutencaoRequest request) {
        Ativo ativo = ativoRepository.findById(request.getAtivoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ativo nao encontrado"));

        if (manutencaoRepository.existeManutencaoAtiva(ativo.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ja existe uma manutencao em andamento para este ativo");
        }

        Manutencao manutencao = new Manutencao();
        manutencao.setAtivo(ativo);
        manutencao.setTipo(request.getTipo());
        manutencao.setCriticidade(request.getCriticidade());
        manutencao.setDtPrevista(request.getDtPrevista());
        manutencao.setDtLimite(request.getDtLimite());
        manutencao.setObservacoes(request.getObservacoes());
        manutencao.setStatus("pendente");
        manutencao.setGeradoAutomaticamente("N");
        manutencao.setHorasUsoNoMomento(ativo.getHorasUsoAcumuladas());

        manutencaoRepository.save(manutencao);

        registrarTransicaoStatus(manutencao, null, "pendente", null, null);

        return toResponse(manutencao);
    }

    public List<ManutencaoResponse> listarFila() {
        return manutencaoRepository.listarFilaPriorizada()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ManutencaoResponse> listarPorAtivo(Long ativoId) {
        return manutencaoRepository.buscarHistoricoDoAtivo(ativoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ManutencaoResponse buscarPorId(Long id) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutencao nao encontrada"));
        return toResponse(manutencao);
    }

    public ManutencaoResponse alterarStatus(Long id, AlterarStatusRequest request) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutencao nao encontrada"));

        String novoStatus = request.getNovoStatus();

        if (novoStatus.equals("cancelada") &&
                (request.getMotivo() == null || request.getMotivo().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O motivo e obrigatorio para cancelar uma manutencao");
        }

        if (novoStatus.equals("em_execucao")) {
            manutencao.setDtInicioExecucao(LocalDateTime.now());
        }

        if (novoStatus.equals("concluida")) {
            manutencao.setDtConclusao(LocalDateTime.now());
        }

        String statusAnterior = manutencao.getStatus();
        manutencao.setStatus(novoStatus);
        manutencaoRepository.save(manutencao);

        registrarTransicaoStatus(manutencao, statusAnterior, novoStatus,
                request.getUsuarioId(), request.getMotivo());

        return toResponse(manutencao);
    }

    public void marcarVencidas() {
        List<Manutencao> vencidas = manutencaoRepository.buscarVencidas(LocalDate.now());
        for (Manutencao m : vencidas) {
            String statusAnterior = m.getStatus();
            m.setStatus("vencida");
            manutencaoRepository.save(m);
            registrarTransicaoStatus(m, statusAnterior, "vencida", null, "Prazo limite ultrapassado");
        }
    }

    private void registrarTransicaoStatus(Manutencao manutencao, String statusAnterior,
                                          String statusNovo, Long usuarioId, String motivo) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setManutencao(manutencao);
        historico.setStatusAnterior(statusAnterior);
        historico.setStatusNovo(statusNovo);
        historico.setDtTransicao(LocalDateTime.now());
        historico.setUsuarioId(usuarioId);
        historico.setMotivo(motivo);
        historicoStatusRepository.save(historico);
    }

    private ManutencaoResponse toResponse(Manutencao m) {
        ManutencaoResponse response = new ManutencaoResponse();
        response.setId(m.getId());
        response.setAtivoId(m.getAtivo().getId());
        response.setNumeroSerieAtivo(m.getAtivo().getNumeroSerie());
        response.setTipo(m.getTipo());
        response.setStatus(m.getStatus());
        response.setCriticidade(m.getCriticidade());
        response.setDtPrevista(m.getDtPrevista());
        response.setDtLimite(m.getDtLimite());
        response.setDtInicioExecucao(m.getDtInicioExecucao());
        response.setDtConclusao(m.getDtConclusao());
        response.setHorasUsoNoMomento(m.getHorasUsoNoMomento());
        response.setObservacoes(m.getObservacoes());
        response.setGeradoAutomaticamente(m.getGeradoAutomaticamente());

        if (m.getTecnico() != null) {
            response.setTecnicoId(m.getTecnico().getId());
            response.setNomeTecnico(m.getTecnico().getNome());
        }

        return response;
    }
}
