package com.sem2026_1.altave_backend.dto;

import java.time.LocalDate;

public class ContratoResumoDTO {
    private Long id;
    private String nomeEmpresa;
    private String status;
    private Long totalOrdens;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer quantidadePlanta;
    private Integer quantidadeAtivos;


    public ContratoResumoDTO(Long id, String nomeEmpresa, String status, Long totalOrdens, LocalDate dataInicio, LocalDate dataFim, Integer quantidadePlanta, Integer quantidadeAtivos) {
        this.id = id;
        this.nomeEmpresa = nomeEmpresa;
        this.status = status;
        this.totalOrdens = totalOrdens;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidadePlanta = quantidadePlanta;
        this.quantidadeAtivos = quantidadeAtivos;
    }

    public Long getId() { 
        return id;
    }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public String getStatus() { return status; }
    public Long getTotalOrdens() { return totalOrdens; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public Integer getQuantidadePlanta() { return quantidadePlanta; }
    public Integer getQuantidadeAtivos() { return quantidadeAtivos; }

}