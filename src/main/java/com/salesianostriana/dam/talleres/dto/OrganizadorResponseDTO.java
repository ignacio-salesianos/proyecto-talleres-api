package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Organizador;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "OrganizadorResponseDTO",
        description = "Datos de un organizador"
)
public record OrganizadorResponseDTO(
        @Schema(description = "Identificador único del organizador", example = "1")
        Long id,

        @Schema(description = "Nombre del organizador", example = "Lucía Fernández")
        String nombre,

        @Schema(description = "Email de contacto del organizador", example = "lucia.fernandez@centrocultural.es")
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
