package com.legacytech.altave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "localizacao")
@Getter
@Setter
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_localizacao")
    @SequenceGenerator(name = "seq_localizacao", sequenceName = "seq_localizacao", allocationSize = 1)
    private Long id;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    // terra ou embarcacao
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    // Em producao sera SDO_GEOMETRY. Para desenvolvimento local usamos lat/lng simples.
    @Column(name = "lat", precision = 10, scale = 7)
    private Double lat;

    @Column(name = "lng", precision = 10, scale = 7)
    private Double lng;
}
