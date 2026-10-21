package com.salesianostriana.dam.talleres.controller;

import com.salesianostriana.dam.talleres.dto.OrganizadorRequestDTO;
import com.salesianostriana.dam.talleres.dto.OrganizadorResponseDTO;
import com.salesianostriana.dam.talleres.service.OrganizadorService;
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
@RequestMapping("/api/v1/organizadores")
@Tag(name = "Organizadores", description = "Gestión de los organizadores de los talleres")
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    @Operation(
            summary = "Obtener todos los organizadores",
            description = "Devuelve la lista de organizadores. Si no hay ninguno, devuelve una lista vacía"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de organizadores",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrganizadorResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "Listado de organizadores",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "nombre": "Lucía Fernández",
                                                "email": "lucia.fernandez@centrocultural.es"
                                              },
                                              {
                                                "id": 2,
                                                "nombre": "Marcos Romero",
                                                "email": "marcos.romero@centrocultural.es"
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<OrganizadorResponseDTO>> getAll() {
        return ResponseEntity.ok(
                organizadorService.findAll()
                        .stream()
                        .map(OrganizadorResponseDTO::of)
                        .toList());
    }

    @Operation(
            summary = "Obtener un organizador por su id",
            description = "Devuelve los datos de un organizador concreto"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Organizador encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrganizadorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe ningún organizador con ese id",
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
                                              "instance": "/api/v1/organizadores/99"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrganizadorResponseDTO> getById(
            @Parameter(description = "Id del organizador", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(OrganizadorResponseDTO.of(organizadorService.findById(id)));
    }

    @Operation(
            summary = "Crear un organizador",
            description = "Crea un nuevo organizador. El nombre y el email son obligatorios"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Organizador creado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrganizadorResponseDTO.class)
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
    public ResponseEntity<OrganizadorResponseDTO> create(@Valid @RequestBody OrganizadorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrganizadorResponseDTO.of(organizadorService.create(dto)));
    }

    @Operation(
            summary = "Editar un organizador",
            description = "Modifica el nombre y el email de un organizador existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Organizador modificado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrganizadorResponseDTO.class)
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
                    description = "No existe ningún organizador con ese id",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrganizadorResponseDTO> edit(
            @Parameter(description = "Id del organizador", example = "1") @PathVariable Long id,
            @Valid @RequestBody OrganizadorRequestDTO dto) {
        return ResponseEntity.ok(OrganizadorResponseDTO.of(organizadorService.edit(id, dto)));
    }

    @Operation(
            summary = "Borrar un organizador",
            description = "Borra un organizador. No se puede borrar si tiene talleres asignados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Organizador borrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe ningún organizador con ese id",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El organizador tiene talleres asignados",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Organizador con talleres",
                                    value = """
                                            {
                                              "type": "https://talleres-api.com/errors/organizador-con-talleres",
                                              "title": "Organizador con talleres",
                                              "status": 409,
                                              "detail": "El organizador con id 1 tiene talleres asignados y no se puede borrar",
                                              "instance": "/api/v1/organizadores/1"
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del organizador", example = "3") @PathVariable Long id) {
        organizadorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
