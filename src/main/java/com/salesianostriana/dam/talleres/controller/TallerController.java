package com.salesianostriana.dam.talleres.controller;

import com.salesianostriana.dam.talleres.dto.TallerRequestDTO;
import com.salesianostriana.dam.talleres.dto.TallerResponseDTO;
import com.salesianostriana.dam.talleres.service.TallerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/talleres")
@Tag(name = "Talleres", description = "Gestión de los talleres del centro cultural")
public class TallerController {

    private final TallerService tallerService;

    @Operation(
            summary = "Obtener todos los talleres",
            description = "Devuelve la lista de talleres con su organizador y sus participantes. Si no hay ninguno, devuelve una lista vacía"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de talleres",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TallerResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "Listado de talleres",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "titulo": "Introducción a la fotografía",
                                                "fechaHoraInicio": "2026-11-03T17:00:00",
                                                "fechaHoraFin": "2026-11-03T19:00:00",
                                                "organizador": {
                                                  "id": 1,
                                                  "nombre": "Lucía Fernández"
                                                },
                                                "participantes": [
                                                  {
                                                    "id": 1,
                                                    "nombreCompleto": "Carlos Martín Ruiz"
                                                  },
                                                  {
                                                    "id": 2,
                                                    "nombreCompleto": "Sara Gómez Pérez"
                                                  }
                                                ]
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<TallerResponseDTO>> getAll() {
        return ResponseEntity.ok(
                tallerService.findAll()
                        .stream()
                        .map(TallerResponseDTO::of)
                        .toList());
    }

    @Operation(
            summary = "Obtener un taller por su id",
            description = "Devuelve los datos de un taller con su organizador y sus participantes"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Taller encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TallerResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe ningún taller con ese id",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Taller no encontrado",
                                    value = """
                                            {
                                              "type": "https://talleres-api.com/errors/not-found",
                                              "title": "Recurso no encontrado",
                                              "status": 404,
                                              "detail": "No existe ningún taller con id 99",
                                              "instance": "/api/v1/talleres/99"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> getById(
            @Parameter(description = "Id del taller", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(TallerResponseDTO.of(tallerService.findById(id)));
    }

    @Operation(
            summary = "Crear un taller",
            description = "Crea un nuevo taller asignado a un organizador existente. Se crea sin participantes. El título no se puede repetir, sin tener en cuenta mayúsculas ni espacios en los extremos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Taller creado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TallerResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Taller creado",
                                    value = """
                                            {
                                              "id": 4,
                                              "titulo": "Iniciación a la acuarela",
                                              "fechaHoraInicio": "2026-11-12T17:00:00",
                                              "fechaHoraFin": "2026-11-12T19:30:00",
                                              "organizador": {
                                                "id": 1,
                                                "nombre": "Lucía Fernández"
                                              },
                                              "participantes": []
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe ningún organizador con el id indicado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Organizador no encontrado",
                                    value = """
                                            {
                                              "type": "https://talleres-api.com/errors/not-found",
                                              "title": "Recurso no encontrado",
                                              "status": 404,
                                              "detail": "No existe ningún organizador con id 99",
                                              "instance": "/api/v1/talleres"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe un taller con ese título",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Taller ya existe",
                                    value = """
                                            {
                                              "type": "https://talleres-api.com/errors/taller-ya-existe",
                                              "title": "Taller ya existe",
                                              "status": 409,
                                              "detail": "Ya existe un taller con el título 'Escritura creativa'",
                                              "instance": "/api/v1/talleres"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<TallerResponseDTO> create(@Valid @RequestBody TallerRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TallerResponseDTO.of(tallerService.create(dto)));
    }

    @Operation(
            summary = "Editar un taller",
            description = "Modifica el título, las fechas y el organizador de un taller existente. Se puede mantener su propio título, pero no poner el de otro taller"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Taller modificado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TallerResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos enviados no son válidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el taller o el organizador indicado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Taller no encontrado",
                                    value = """
                                            {
                                              "type": "https://talleres-api.com/errors/not-found",
                                              "title": "Recurso no encontrado",
                                              "status": 404,
                                              "detail": "No existe ningún taller con id 99",
                                              "instance": "/api/v1/talleres/99"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe otro taller con ese título",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Taller ya existe",
                                    value = """
                                            {
                                              "type": "https://talleres-api.com/errors/taller-ya-existe",
                                              "title": "Taller ya existe",
                                              "status": 409,
                                              "detail": "Ya existe un taller con el título 'Introducción a la fotografía'",
                                              "instance": "/api/v1/talleres/3"
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> edit(
            @Parameter(description = "Id del taller", example = "1") @PathVariable Long id,
            @Valid @RequestBody TallerRequestDTO dto) {
        return ResponseEntity.ok(TallerResponseDTO.of(tallerService.edit(id, dto)));
    }

    @Operation(
            summary = "Borrar un taller",
            description = "Borra un taller y sus inscripciones. Los participantes no se borran"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Taller borrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe ningún taller con ese id",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Taller no encontrado",
                                    value = """
                                            {
                                              "type": "https://talleres-api.com/errors/not-found",
                                              "title": "Recurso no encontrado",
                                              "status": 404,
                                              "detail": "No existe ningún taller con id 99",
                                              "instance": "/api/v1/talleres/99"
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del taller", example = "3") @PathVariable Long id) {
        tallerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
