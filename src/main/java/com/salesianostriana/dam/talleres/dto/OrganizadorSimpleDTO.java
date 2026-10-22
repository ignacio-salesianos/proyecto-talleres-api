package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Organizador;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "OrganizadorSimpleDTO",
        description = "Datos básicos del organizador de un taller"
)
public record OrganizadorSimpleDTO(
        @Schema(description = "Identificador único del organizador", example = "1")
        Long id,

        @Schema(description = "Nombre del organizador", example = "Lucía Fernández")
        String nombre
) {

    public static OrganizadorSimpleDTO of(Organizador o) {
        return new OrganizadorSimpleDTO(
                o.getId(),
                o.getNombre()
        );
    }

}
