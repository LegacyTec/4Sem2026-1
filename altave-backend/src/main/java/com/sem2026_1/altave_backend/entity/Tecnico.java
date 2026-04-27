package com.sem2026_1.altave_backend.entity;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tecnico")
public class Tecnico {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tecnico_id")
    private Long id;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "cargo")
    private String cargo;

    @ManyToMany(mappedBy = "tecnicos")
    private Set<Contrato> contratos;

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

    public Set<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(Set<Contrato> contratos) {
        this.contratos = contratos;
    }


}
