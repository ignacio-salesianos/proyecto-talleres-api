package com.salesianostriana.dam.talleres.error.notfound;

public class TallerNoEncontradoException extends EntityNotFoundException {

    public TallerNoEncontradoException(Long id) {
        super("No existe ningún taller con id %d".formatted(id));
    }

}
