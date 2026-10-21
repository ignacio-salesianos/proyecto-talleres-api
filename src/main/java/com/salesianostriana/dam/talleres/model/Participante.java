package com.salesianostriana.dam.talleres.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto;

    @ManyToMany(mappedBy = "participantes")
    @Builder.Default
    private List<Taller> talleres = new ArrayList<>();

}
