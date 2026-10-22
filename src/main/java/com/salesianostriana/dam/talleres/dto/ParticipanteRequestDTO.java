package com.salesianostriana.dam.talleres.dto;

import com.salesianostriana.dam.talleres.model.Participante;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "ParticipanteRequestDTO",
        description = "Datos para dar de alta un participante"
)
public record ParticipanteRequestDTO(
        @Schema(description = "Nombre completo del participante", example = "Laura Sánchez Vidal")
        @NotBlank(message = "{participanteRequestDTO.nombreCompleto.notBlank}")
        @Size(max = 150, message = "{participanteRequestDTO.nombreCompleto.size}")
        String nombreCompleto
) {

    public Participante to() {
        return Participante.builder()
                .nombreCompleto(nombreCompleto.trim())
                .build();
    }

}
