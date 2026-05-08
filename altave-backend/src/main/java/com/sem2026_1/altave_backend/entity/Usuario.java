package com.sem2026_1.altave_backend.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import com.sem2026_1.altave_backend.controller.View;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    @JsonView({View.Contrato.class, View.Ordem.class})
    private Long id;

    @Column(name = "nome_completo")
    @JsonView({View.Contrato.class, View.Ordem.class})
    private String nomeCompleto;

    @Column(name = "email")
    @JsonView({View.Contrato.class, View.Ordem.class})
    private String email;

    @Column(name = "status")
    @JsonView(View.Contrato.class)
    private String status;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "cargo")
    @JsonView({View.Contrato.class, View.Ordem.class})
    private String cargo;

    @Column(name = "funcao")
    @JsonView({View.Contrato.class, View.Ordem.class})
    private String funcao;

    @ManyToMany(mappedBy = "usuarios")
    private Set<Contrato> contratos;

    @ManyToMany(mappedBy = "usuarios")
    private Set<OrdemManutencao> ordens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public Set<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(Set<Contrato> contratos) {
        this.contratos = contratos;
    }


}
