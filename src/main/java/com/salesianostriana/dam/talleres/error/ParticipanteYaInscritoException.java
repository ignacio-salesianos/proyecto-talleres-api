package com.salesianostriana.dam.talleres.error;

public class ParticipanteYaInscritoException extends RuntimeException {

    public ParticipanteYaInscritoException(Long participanteId, Long tallerId) {
        super("El participante con id %d ya está inscrito en el taller con id %d".formatted(participanteId, tallerId));
    }

}
