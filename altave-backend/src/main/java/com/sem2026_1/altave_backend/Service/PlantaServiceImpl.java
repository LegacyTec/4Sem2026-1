package com.sem2026_1.altave_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sem2026_1.altave_backend.dto.PlantaCreateDTO;
import com.sem2026_1.altave_backend.entity.Contrato;
import com.sem2026_1.altave_backend.entity.Planta;
import com.sem2026_1.altave_backend.repository.ContratoRepository;
import com.sem2026_1.altave_backend.repository.PlantaRepository;

import jakarta.transaction.Transactional;

@Service
public class PlantaServiceImpl implements PlantaService {

    private final PlantaRepository plantaRepository;
    private final ContratoRepository contratoRepository;

    public PlantaServiceImpl(PlantaRepository plantaRepository, ContratoRepository contratoRepository) {
        this.plantaRepository = plantaRepository;
        this.contratoRepository = contratoRepository;
    }

    @Override
    public List<Planta> listarPorContrato(Long contratoId) {
        return plantaRepository.findByContratoId(contratoId);
    }

    @Override
    @Transactional
    public Planta criar(PlantaCreateDTO dto) {
        if (dto.idContrato() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idContrato é obrigatório");
        }

        Contrato contrato = contratoRepository.findById(dto.idContrato())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contrato não encontrado: " + dto.idContrato()));

        Planta planta = new Planta();
        planta.setNome(dto.nome());
        planta.setContrato(contrato);
        planta.setLatitude(dto.latitude());
        planta.setLongitude(dto.longitude());

        return plantaRepository.save(planta);
    }
}
