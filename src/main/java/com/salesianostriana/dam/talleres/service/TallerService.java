package com.salesianostriana.dam.talleres.service;

import com.salesianostriana.dam.talleres.dto.TallerRequestDTO;
import com.salesianostriana.dam.talleres.error.ParticipanteYaInscritoException;
import com.salesianostriana.dam.talleres.error.TallerYaExisteException;
import com.salesianostriana.dam.talleres.error.notfound.OrganizadorNoEncontradoException;
import com.salesianostriana.dam.talleres.error.notfound.ParticipanteNoEncontradoException;
import com.salesianostriana.dam.talleres.error.notfound.TallerNoEncontradoException;
import com.salesianostriana.dam.talleres.model.Organizador;
import com.salesianostriana.dam.talleres.model.Participante;
import com.salesianostriana.dam.talleres.model.Taller;
import com.salesianostriana.dam.talleres.repository.OrganizadorRepository;
import com.salesianostriana.dam.talleres.repository.ParticipanteRepository;
import com.salesianostriana.dam.talleres.repository.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TallerService {

    private final TallerRepository tallerRepository;
    private final OrganizadorRepository organizadorRepository;
    private final ParticipanteRepository participanteRepository;

    public List<Taller> findAll() {
        return tallerRepository.findAll();
    }

    public Taller findById(Long id) {
        return tallerRepository.findById(id)
                .orElseThrow(() -> new TallerNoEncontradoException(id));
    }

    public Taller create(TallerRequestDTO dto) {
        if (tallerRepository.existsByTituloIgnoreCase(dto.titulo().trim())) {
            throw new TallerYaExisteException(dto.titulo().trim());
        }
        Taller taller = dto.to();
        taller.setOrganizador(findOrganizador(dto.organizadorId()));
        return tallerRepository.save(taller);
    }

    public Taller edit(Long id, TallerRequestDTO dto) {
        Taller taller = findById(id);
        if (tallerRepository.existsByTituloIgnoreCaseAndIdNot(dto.titulo().trim(), id)) {
            throw new TallerYaExisteException(dto.titulo().trim());
        }
        taller.setTitulo(dto.titulo().trim());
        taller.setFechaHoraInicio(dto.fechaHoraInicio());
        taller.setFechaHoraFin(dto.fechaHoraFin());
        taller.setOrganizador(findOrganizador(dto.organizadorId()));
        return tallerRepository.save(taller);
    }

    public void delete(Long id) {
        Taller taller = findById(id);
        tallerRepository.delete(taller);
    }

    public Taller inscribir(Long tallerId, Long participanteId) {
        Taller taller = findById(tallerId);
        Participante participante = findParticipante(participanteId);

        boolean yaInscrito = taller.getParticipantes().stream()
                .anyMatch(p -> p.getId().equals(participanteId));
        if (yaInscrito) {
            throw new ParticipanteYaInscritoException(participanteId, tallerId);
        }

        taller.getParticipantes().add(participante);
        return tallerRepository.save(taller);
    }

    public List<Participante> findParticipantes(Long tallerId) {
        return findById(tallerId).getParticipantes();
    }

    public void desinscribir(Long tallerId, Long participanteId) {
        Taller taller = findById(tallerId);
        findParticipante(participanteId);

        boolean eliminado = taller.getParticipantes()
                .removeIf(p -> p.getId().equals(participanteId));
        if (eliminado) {
            tallerRepository.save(taller);
        }
    }

    private Organizador findOrganizador(Long organizadorId) {
        return organizadorRepository.findById(organizadorId)
                .orElseThrow(() -> new OrganizadorNoEncontradoException(organizadorId));
    }

    private Participante findParticipante(Long participanteId) {
        return participanteRepository.findById(participanteId)
                .orElseThrow(() -> new ParticipanteNoEncontradoException(participanteId));
    }

}
