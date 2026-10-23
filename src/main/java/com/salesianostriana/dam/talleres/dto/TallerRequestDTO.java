package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Taller;
import com.salesianostriana.dam.talleres.validation.FechaFinPosterior;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(
        name = "TallerRequestDTO",
        description = "Datos para crear o editar un taller"
)
@FechaFinPosterior(message = "{tallerRequestDTO.fechaHoraFin.posterior}")
public record TallerRequestDTO(
        @Schema(description = "Título del taller, no se puede repetir", example = "Iniciación a la acuarela")
        @NotBlank(message = "{tallerRequestDTO.titulo.notBlank}")
        @Size(max = 150, message = "{tallerRequestDTO.titulo.size}")
        String titulo,

        @Schema(description = "Fecha y hora de inicio", example = "2026-11-12T17:00:00")
        @NotNull(message = "{tallerRequestDTO.fechaHoraInicio.notNull}")
        LocalDateTime fechaHoraInicio,

        @Schema(description = "Fecha y hora de fin, tiene que ser posterior a la de inicio", example = "2026-11-12T19:30:00")
        @NotNull(message = "{tallerRequestDTO.fechaHoraFin.notNull}")
        LocalDateTime fechaHoraFin,

        @Schema(description = "Id del organizador del taller", example = "1")
        @NotNull(message = "{tallerRequestDTO.organizadorId.notNull}")
        Long organizadorId
) {

    public Taller to() {
        return Taller.builder()
                .titulo(titulo.trim())
                .fechaHoraInicio(fechaHoraInicio)
                .fechaHoraFin(fechaHoraFin)
                .build();
    }

}
