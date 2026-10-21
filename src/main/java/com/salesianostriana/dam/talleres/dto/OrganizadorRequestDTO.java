package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Organizador;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrganizadorRequestDTO(
        @NotBlank(message = "{organizadorRequestDTO.nombre.notBlank}")
        @Size(max = 100, message = "{organizadorRequestDTO.nombre.size}")
        String nombre,

        @NotBlank(message = "{organizadorRequestDTO.email.notBlank}")
        @Email(message = "{organizadorRequestDTO.email.email}")
        String email
) {

    public Organizador to() {
        return Organizador.builder()
                .nombre(nombre.trim())
                .email(email.trim())
                .build();
    }

}
