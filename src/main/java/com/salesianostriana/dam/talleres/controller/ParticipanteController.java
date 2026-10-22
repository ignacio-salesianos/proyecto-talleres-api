package com.salesianostriana.dam.talleres.controller;

import com.salesianostriana.dam.talleres.dto.ParticipanteRequestDTO;
import com.salesianostriana.dam.talleres.dto.ParticipanteSimpleDTO;
import com.salesianostriana.dam.talleres.service.ParticipanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participantes")
@Tag(name = "Participantes", description = "Alta y listado de las personas que se inscriben en los talleres")
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @Operation(
            summary = "Obtener todos los participantes",
            description = "Devuelve la lista de participantes. Si no hay ninguno, devuelve una lista vacía"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de participantes",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ParticipanteSimpleDTO.class)),
                            examples = @ExampleObject(
                                    name = "Listado de participantes",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "nombreCompleto": "Carlos Martín Ruiz"
                                              },
                                              {
                                                "id": 2,
                                                "nombreCompleto": "Sara Gómez Pérez"
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ParticipanteSimpleDTO>> getAll() {
        return ResponseEntity.ok(
                participanteService.findAll()
                        .stream()
                        .map(ParticipanteSimpleDTO::of)
                        .toList());
    }

    @Operation(
            summary = "Dar de alta un participante",
            description = "Crea un nuevo participante. El nombre completo es obligatorio"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Participante creado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipanteSimpleDTO.class),
                            examples = @ExampleObject(
                                    name = "Participante creado",
                                    value = """
                                            {
                                              "id": 5,
                                              "nombreCompleto": "Laura Sánchez Vidal"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos enviados no son válidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ParticipanteSimpleDTO> create(@Valid @RequestBody ParticipanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ParticipanteSimpleDTO.of(participanteService.create(dto)));
    }

}
