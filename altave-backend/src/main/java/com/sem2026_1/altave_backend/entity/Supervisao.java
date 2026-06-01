package com.sem2026_1.altave_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.controller.View;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "supervisao")
public class Supervisao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_supervisao")
    @JsonView({View.Contrato.class, View.Ordem.class})
    private Long id;

    @Column(name = "nome", nullable = false)
    @JsonView({View.Contrato.class, View.Ordem.class})
    private String nome;

    @Column(name = "descricao")
    @JsonView(View.Contrato.class)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    @JsonIgnore
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_supervisor")
    @JsonView(View.Contrato.class)
    private Usuario supervisor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "supervisao_planta",
        joinColumns = @JoinColumn(name = "id_supervisao"),
        inverseJoinColumns = @JoinColumn(name = "id_planta")
    )
    @JsonView(View.Contrato.class)
    private List<Planta> plantas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }
    public Usuario getSupervisor() { return supervisor; }
    public void setSupervisor(Usuario supervisor) { this.supervisor = supervisor; }
    public List<Planta> getPlantas() { return plantas; }
    public void setPlantas(List<Planta> plantas) { this.plantas = plantas; }
}
