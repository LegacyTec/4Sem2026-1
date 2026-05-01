package com.sem2026_1.altave_backend.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;
import org.springframework.cglib.core.Local;

import com.sem2026_1.altave_backend.entity.enums.StatusOrdem;
import com.sem2026_1.altave_backend.entity.enums.TipoManutencao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordem_manutencao")
public class OrdemManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ordem")
    private Long id;
    
    @Column(name="nome_ordem")
    private String nome;

    @Column(name="descricao")
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name="data_fim")
    private LocalDate dataFim;

    @Column(name = "tipo_manutencao")
    @Enumerated(EnumType.STRING)
    private TipoManutencao tipoManutencao;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private StatusOrdem status;

    @Column(name = "comentario")
    private String comentario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ativo")
    private Ativo ativo;

    @ManyToMany(mappedBy = "ordens")
    private Set<Usuario> usuarios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public TipoManutencao getTipoOrdem() {
        return tipoManutencao;
    }

    public void setTipoOrdem(TipoManutencao tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
    }

    public StatusOrdem getStatus() {
        return status;
    }

    public void setStatus(StatusOrdem status) {
        this.status = status;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public TipoManutencao getTipoManutencao() {
        return tipoManutencao;
    }

    public void setTipoManutencao(TipoManutencao tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }


}
