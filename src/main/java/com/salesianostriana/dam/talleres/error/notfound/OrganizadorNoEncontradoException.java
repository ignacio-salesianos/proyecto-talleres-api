package com.salesianostriana.dam.talleres.error.notfound;

public class OrganizadorNoEncontradoException extends EntityNotFoundException {

    public OrganizadorNoEncontradoException(Long id) {
        super("No existe ningún organizador con id %d".formatted(id));
    }

}
