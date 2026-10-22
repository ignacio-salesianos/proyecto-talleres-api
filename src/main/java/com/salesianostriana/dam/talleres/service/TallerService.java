package com.salesianostriana.dam.talleres.service;

import com.salesianostriana.dam.talleres.dto.TallerRequestDTO;
import com.salesianostriana.dam.talleres.error.TallerYaExisteException;
import com.salesianostriana.dam.talleres.error.notfound.OrganizadorNoEncontradoException;
import com.salesianostriana.dam.talleres.error.notfound.TallerNoEncontradoException;
import com.salesianostriana.dam.talleres.model.Organizador;
import com.salesianostriana.dam.talleres.model.Taller;
import com.salesianostriana.dam.talleres.repository.OrganizadorRepository;
import com.salesianostriana.dam.talleres.repository.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TallerService {

    private final TallerRepository tallerRepository;
    private final OrganizadorRepository organizadorRepository;

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

    private Organizador findOrganizador(Long organizadorId) {
        return organizadorRepository.findById(organizadorId)
                .orElseThrow(() -> new OrganizadorNoEncontradoException(organizadorId));
    }

}
