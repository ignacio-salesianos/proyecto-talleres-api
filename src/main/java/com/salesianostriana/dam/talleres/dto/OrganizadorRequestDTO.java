package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Organizador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "OrganizadorRequestDTO",
        description = "Datos para crear o editar un organizador"
)
public record OrganizadorRequestDTO(
        @Schema(description = "Nombre del organizador", example = "Lucía Fernández")
        @NotBlank(message = "{organizadorRequestDTO.nombre.notBlank}")
        @Size(max = 100, message = "{organizadorRequestDTO.nombre.size}")
        String nombre,

        @Schema(description = "Email de contacto del organizador", example = "lucia.fernandez@centrocultural.es")
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
