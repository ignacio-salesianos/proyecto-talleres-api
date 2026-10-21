package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Organizador;

public record OrganizadorResponseDTO(
        Long id,
        String nombre,
        String email
) {

    public static OrganizadorResponseDTO of(Organizador o) {
        return new OrganizadorResponseDTO(
                o.getId(),
                o.getNombre(),
                o.getEmail()
        );
    }

}
