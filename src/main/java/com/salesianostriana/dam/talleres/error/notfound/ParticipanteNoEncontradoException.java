package com.salesianostriana.dam.talleres.error.notfound;

public class ParticipanteNoEncontradoException extends EntityNotFoundException {

    public ParticipanteNoEncontradoException(Long id) {
        super("No existe ningún participante con id %d".formatted(id));
    }

}
