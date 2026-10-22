package com.salesianostriana.dam.talleres.repository;

import com.salesianostriana.dam.talleres.model.Taller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TallerRepository extends JpaRepository<Taller, Long> {

    boolean existsByOrganizadorId(Long organizadorId);

    boolean existsByTituloIgnoreCase(String titulo);

    boolean existsByTituloIgnoreCaseAndIdNot(String titulo, Long id);

}
