package com.sem2026_1.altave_backend.entity;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.query.common.FetchClauseType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ativo")
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ativo")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "fabricante")
    private String fabricante;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "periodicidade_manutencao")
    private Long periodicidadeManutencao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_instalacao")
    private LocalDate dataInstalacao;

    @Column(name="data_remocao")
    private LocalDate dataRemocao;

    @Column(name = "predio")
    private String predio;

    @Column(name = "planta")
    private String planta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_contrato")
    private Contrato contrato;

    @OneToMany(mappedBy = "ativo", fetch = FetchType.LAZY)
    private Set<OrdemManutencao> ordens;

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Set<OrdemManutencao> getOrdens() {
        return ordens;
    }

    public void setOrdens(Set<OrdemManutencao> ordens) {
        this.ordens = ordens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getPeriodicidadeManutencao() {
        return periodicidadeManutencao;
    }

    public void setPeriodicidadeManutencao(Long periodicidadeManutencao) {
        this.periodicidadeManutencao = periodicidadeManutencao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInstalacao() {
        return dataInstalacao;
    }

    public void setDataInstalacao(LocalDate dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }

    public LocalDate getDataRemocao() {
        return dataRemocao;
    }

    public void setDataRemocao(LocalDate dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getPredio() {
        return predio;
    }

    public void setPredio(String predio) {
        this.predio = predio;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }
    
}
