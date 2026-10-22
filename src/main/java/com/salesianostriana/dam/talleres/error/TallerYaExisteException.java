package com.salesianostriana.dam.talleres.error;

public class TallerYaExisteException extends RuntimeException {

    public TallerYaExisteException(String titulo) {
        super("Ya existe un taller con el título '%s'".formatted(titulo));
    }

}
