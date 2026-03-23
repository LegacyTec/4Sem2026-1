package com.legacytech.altave.repository;

import com.legacytech.altave.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    // Busca tecnicos disponiveis
    List<Tecnico> findByStatus(String status);

    // Busca por nivel
    List<Tecnico> findByNivel(String nivel);

    // Tecnicos disponiveis com uma certificacao especifica e valida
    @Query("SELECT t FROM Tecnico t " +
           "JOIN TecnicoCertificacao tc ON tc.id.tecnicoId = t.id " +
           "WHERE t.status = 'disponivel' " +
           "AND tc.id.certificacaoId = :certificacaoId " +
           "AND tc.dtVencimento >= CURRENT_DATE")
    List<Tecnico> buscarDisponiveisComCertificacao(@Param("certificacaoId") Long certificacaoId);
}
