package com.salesianostriana.dam.talleres.service;

import com.salesianostriana.dam.talleres.dto.ParticipanteRequestDTO;
import com.salesianostriana.dam.talleres.error.notfound.ParticipanteNoEncontradoException;
import com.salesianostriana.dam.talleres.model.Participante;
import com.salesianostriana.dam.talleres.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    public List<Participante> findAll() {
        return participanteRepository.findAll();
    }

    public Participante findById(Long id) {
        return participanteRepository.findById(id)
                .orElseThrow(() -> new ParticipanteNoEncontradoException(id));
    }

    public Participante create(ParticipanteRequestDTO dto) {
        return participanteRepository.save(dto.to());
    }

}
