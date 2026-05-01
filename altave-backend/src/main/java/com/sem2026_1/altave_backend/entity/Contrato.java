package com.sem2026_1.altave_backend.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.controller.View;
import com.sem2026_1.altave_backend.entity.enums.StatusContrato;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="contrato")
public class Contrato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_contrato")
    @JsonView(View.Contrato.class)
    private Long id;

    @Column(name = "nome_empresa")
    @JsonView(View.Contrato.class)
    private String nomeEmpresa;


    @Column(name = "quantidade_plantas")
    @JsonView(View.Contrato.class)
    private Integer quantidadePlanta;

    @Column(name = "data_inicio")
    @JsonView(View.Contrato.class)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    @JsonView(View.Contrato.class)
    private LocalDate dataFim;

    @Column(name = "quantidade_ativos")
    @JsonView(View.Contrato.class)
    private Integer quantidadeAtivos;

    @Column(name = "descricao")
    @JsonView(View.Contrato.class)
    private String descricao;

    @Column(name = "quantidade_supervisao")
    private Integer quantidadeSupervisao;

    @Column(name = "criador")
    @JsonView(View.Contrato.class)
    private String criador;

    @Column(name = "status")
    @JsonView(View.Contrato.class)
    @Enumerated(EnumType.STRING)
    private StatusContrato status;

    @ManyToMany
    @JoinTable(name = "contrato_usuario",
        joinColumns = {@JoinColumn(name = "id_contrato")},
        inverseJoinColumns = {@JoinColumn(name="id_usuario")}
    )
    @JsonView(View.Contrato.class)
    private Set<Usuario> usuarios;

    @OneToMany(mappedBy = "contrato", fetch = FetchType.LAZY)
    private Set<Ativo> ativos;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public Integer getQuantidadePlanta() {
        return quantidadePlanta;
    }

    public void setQuantidadePlanta(Integer quantidadePlanta) {
        this.quantidadePlanta = quantidadePlanta;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getQuantidadeAtivos() {
        return quantidadeAtivos;
    }

    public void setQuantidadeAtivos(Integer quantidadeAtivos) {
        this.quantidadeAtivos = quantidadeAtivos;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Integer getQuantidadeSupervisao() {
        return quantidadeSupervisao;
    }

    public void setQuantidadeSupervisao(Integer quantidadeSupervisao) {
        this.quantidadeSupervisao = quantidadeSupervisao;
    }

    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    public StatusContrato getStatus() {
        return status;
    }

    public void setStatus(StatusContrato status) {
        this.status = status;
    }

    public Set<Ativo> getAtivos() {
        return ativos;
    }

    public void setAtivos(Set<Ativo> ativos) {
        this.ativos = ativos;
    }
  
    
}
