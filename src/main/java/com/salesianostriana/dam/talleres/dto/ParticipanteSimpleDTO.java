package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Participante;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ParticipanteSimpleDTO",
        description = "Datos básicos de un participante inscrito"
)
public record ParticipanteSimpleDTO(
        @Schema(description = "Identificador único del participante", example = "1")
        Long id,

        @Schema(description = "Nombre completo del participante", example = "Carlos Martín Ruiz")
        String nombreCompleto
) {

    public static ParticipanteSimpleDTO of(Participante p) {
        return new ParticipanteSimpleDTO(
                p.getId(),
                p.getNombreCompleto()
        );
    }

}
