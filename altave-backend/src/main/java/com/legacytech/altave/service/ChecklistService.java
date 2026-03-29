package com.legacytech.altave.service;

import com.legacytech.altave.dto.ChecklistExecucaoRequest;
import com.legacytech.altave.dto.ChecklistExecucaoResponse;
import com.legacytech.altave.model.ChecklistExecucao;
import com.legacytech.altave.model.OrdemServico;
import com.legacytech.altave.repository.ChecklistExecucaoRepository;
import com.legacytech.altave.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistExecucaoRepository checklistExecucaoRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public List<ChecklistExecucaoResponse> listarPorOrdem(Long ordemServicoId) {
        ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ordem de servico nao encontrada"));

        List<ChecklistExecucao> execucoes = 
                checklistExecucaoRepository.findByOrdemServicoId(ordemServicoId);
        
            return execucoes.stream()
                .map(this::toResponse)
                .toList();
    }

    public ChecklistExecucaoResponse atualizarItem(Long itemExecucaoId, ChecklistExecucaoRequest request) {
        ChecklistExecucao execucao = checklistExecucaoRepository.findById(itemExecucaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Item do checklist nao encontrado"));

        execucao.setConcluido(request.getConcluido() ? "S" : "N");
        execucao.setObservacao(request.getObservacao());
        execucao.setDtRegistro(LocalDateTime.now());

        checklistExecucaoRepository.save(execucao);
        return toResponse(execucao);
    }

    private ChecklistExecucaoResponse toResponse(ChecklistExecucao execucao) {
        ChecklistExecucaoResponse response = new ChecklistExecucaoResponse();
        response.setId(execucao.getId());
        response.setObservacao(execucao.getObservacao());
        response.setDtRegistro(execucao.getDtRegistro());
        response.setConcluido(execucao.getConcluido().equals("S"));

        response.setDescricaoItem(execucao.getItem().getDescricao());
        response.setObrigatorioItem(execucao.getItem().getObrigatorio().equals("S") ? "Sim" : "Nao");
        
        return response;
    }

    public boolean todosObrigatoriosConcluidos(Long ordemServicoId) {
        return !checklistExecucaoRepository.existeItemObrigatorioNaoConcluido(ordemServicoId);
    }
}
