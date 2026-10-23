package com.salesianostriana.dam.talleres.error;

public class OrganizadorConTalleresException extends RuntimeException {

    public OrganizadorConTalleresException(Long id) {
        super("El organizador con id %d tiene talleres asignados y no se puede borrar".formatted(id));
    }

}
