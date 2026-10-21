package com.salesianostriana.dam.talleres.service;

import com.salesianostriana.dam.talleres.dto.OrganizadorRequestDTO;
import com.salesianostriana.dam.talleres.error.OrganizadorConTalleresException;
import com.salesianostriana.dam.talleres.error.notfound.OrganizadorNoEncontradoException;
import com.salesianostriana.dam.talleres.model.Organizador;
import com.salesianostriana.dam.talleres.repository.OrganizadorRepository;
import com.salesianostriana.dam.talleres.repository.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizadorService {

    private final OrganizadorRepository organizadorRepository;
    private final TallerRepository tallerRepository;

    public List<Organizador> findAll() {
        return organizadorRepository.findAll();
    }

    public Organizador findById(Long id) {
        return organizadorRepository.findById(id)
                .orElseThrow(() -> new OrganizadorNoEncontradoException(id));
    }

    public Organizador create(OrganizadorRequestDTO dto) {
        return organizadorRepository.save(dto.to());
    }

    public Organizador edit(Long id, OrganizadorRequestDTO dto) {
        Organizador organizador = findById(id);
        organizador.setNombre(dto.nombre().trim());
        organizador.setEmail(dto.email().trim());
        return organizadorRepository.save(organizador);
    }

    /*
        No se permite borrar un organizador que tenga talleres,
        porque cada taller tiene que tener siempre un organizador.
        Antes hay que borrar sus talleres o asignarlos a otro.
     */
    public void delete(Long id) {
        Organizador organizador = findById(id);
        if (tallerRepository.existsByOrganizadorId(id)) {
            throw new OrganizadorConTalleresException(id);
        }
        organizadorRepository.delete(organizador);
    }

}
