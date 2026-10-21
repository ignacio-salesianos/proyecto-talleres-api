package com.salesianostriana.dam.talleres.utils;

import com.salesianostriana.dam.talleres.model.Organizador;
import com.salesianostriana.dam.talleres.model.Participante;
import com.salesianostriana.dam.talleres.model.Taller;
import com.salesianostriana.dam.talleres.repository.OrganizadorRepository;
import com.salesianostriana.dam.talleres.repository.ParticipanteRepository;
import com.salesianostriana.dam.talleres.repository.TallerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeed {

    private final OrganizadorRepository organizadorRepository;
    private final TallerRepository tallerRepository;
    private final ParticipanteRepository participanteRepository;

    @PostConstruct
    public void initData() {

        /*
            Datos de ejemplo para poder probar la API con Postman.
            El organizador 3 no tiene talleres, así que se puede borrar.
         */

        Organizador o1 = Organizador.builder()
                .nombre("Lucía Fernández")
                .email("lucia.fernandez@centrocultural.es")
                .build();

        Organizador o2 = Organizador.builder()
                .nombre("Marcos Romero")
                .email("marcos.romero@centrocultural.es")
                .build();

        Organizador o3 = Organizador.builder()
                .nombre("Elena Castillo")
                .email("elena.castillo@centrocultural.es")
                .build();

        organizadorRepository.saveAll(List.of(o1, o2, o3));

        Participante p1 = Participante.builder().nombreCompleto("Carlos Martín Ruiz").build();
        Participante p2 = Participante.builder().nombreCompleto("Sara Gómez Pérez").build();
        Participante p3 = Participante.builder().nombreCompleto("Javier Ortega León").build();
        Participante p4 = Participante.builder().nombreCompleto("Marta Delgado Sanz").build();

        participanteRepository.saveAll(List.of(p1, p2, p3, p4));

        Taller t1 = Taller.builder()
                .titulo("Introducción a la fotografía")
                .fechaHoraInicio(LocalDateTime.of(2026, 11, 3, 17, 0))
                .fechaHoraFin(LocalDateTime.of(2026, 11, 3, 19, 0))
                .organizador(o1)
                .build();

        Taller t2 = Taller.builder()
                .titulo("Cerámica para principiantes")
                .fechaHoraInicio(LocalDateTime.of(2026, 11, 5, 10, 0))
                .fechaHoraFin(LocalDateTime.of(2026, 11, 5, 13, 0))
                .organizador(o1)
                .build();

        Taller t3 = Taller.builder()
                .titulo("Escritura creativa")
                .fechaHoraInicio(LocalDateTime.of(2026, 11, 10, 18, 0))
                .fechaHoraFin(LocalDateTime.of(2026, 11, 10, 20, 0))
                .organizador(o2)
                .build();

        t1.addParticipante(p1);
        t1.addParticipante(p2);
        t2.addParticipante(p1);
        t3.addParticipante(p3);

        tallerRepository.saveAll(List.of(t1, t2, t3));

    }

}
