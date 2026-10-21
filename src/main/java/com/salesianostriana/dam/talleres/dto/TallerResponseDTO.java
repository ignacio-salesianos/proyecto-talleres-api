package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Taller;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
        name = "TallerResponseDTO",
        description = "Datos de un taller con su organizador y sus participantes"
)
public record TallerResponseDTO(
        @Schema(description = "Identificador único del taller", example = "1")
        Long id,

        @Schema(description = "Título del taller", example = "Introducción a la fotografía")
        String titulo,

        @Schema(description = "Fecha y hora de inicio", example = "2026-11-03T17:00:00")
        LocalDateTime fechaHoraInicio,

        @Schema(description = "Fecha y hora de fin", example = "2026-11-03T19:00:00")
        LocalDateTime fechaHoraFin,

        @Schema(description = "Organizador del taller")
        OrganizadorSimpleDTO organizador,

        @ArraySchema(
                arraySchema = @Schema(description = "Participantes inscritos, vacía si no hay ninguno"),
                schema = @Schema(implementation = ParticipanteSimpleDTO.class)
        )
        List<ParticipanteSimpleDTO> participantes
) {

    public static TallerResponseDTO of(Taller t) {
        return new TallerResponseDTO(
                t.getId(),
                t.getTitulo(),
                t.getFechaHoraInicio(),
                t.getFechaHoraFin(),
                t.getOrganizador() != null ? OrganizadorSimpleDTO.of(t.getOrganizador()) : null,
                t.getParticipantes() != null
                        ? t.getParticipantes().stream().map(ParticipanteSimpleDTO::of).toList()
                        : List.of()
        );
    }

}
