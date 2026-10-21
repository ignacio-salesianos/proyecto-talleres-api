package com.salesianostriana.dam.talleres.controller;

import com.salesianostriana.dam.talleres.dto.OrganizadorRequestDTO;
import com.salesianostriana.dam.talleres.dto.OrganizadorResponseDTO;
import com.salesianostriana.dam.talleres.service.OrganizadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizadores")
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    @GetMapping
    public ResponseEntity<List<OrganizadorResponseDTO>> getAll() {
        return ResponseEntity.ok(
                organizadorService.findAll()
                        .stream()
                        .map(OrganizadorResponseDTO::of)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizadorResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(OrganizadorResponseDTO.of(organizadorService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrganizadorResponseDTO> create(@Valid @RequestBody OrganizadorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrganizadorResponseDTO.of(organizadorService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizadorResponseDTO> edit(@PathVariable Long id,
                                                       @Valid @RequestBody OrganizadorRequestDTO dto) {
        return ResponseEntity.ok(OrganizadorResponseDTO.of(organizadorService.edit(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizadorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
