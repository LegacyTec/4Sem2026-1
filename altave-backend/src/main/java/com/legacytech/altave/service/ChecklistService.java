package com.legacytech.altave.service;

import com.legacytech.altave.dto.ChecklistExecucaoRequest;
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

    public List<ChecklistExecucao> listarPorOrdem(Long ordemServicoId) {
        ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ordem de servico nao encontrada"));

        return checklistExecucaoRepository.findByOrdemServicoId(ordemServicoId);
    }

    public ChecklistExecucao atualizarItem(Long itemExecucaoId, ChecklistExecucaoRequest request) {
        ChecklistExecucao execucao = checklistExecucaoRepository.findById(itemExecucaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Item do checklist nao encontrado"));

        execucao.setConcluido(request.getConcluido());
        execucao.setObservacao(request.getObservacao());
        execucao.setDtRegistro(LocalDateTime.now());

        return checklistExecucaoRepository.save(execucao);
    }

    public boolean todosObrigatoriosConcluidos(Long ordemServicoId) {
        return !checklistExecucaoRepository.existeItemObrigatorioNaoConcluido(ordemServicoId);
    }
}
