package com.salesianostriana.dam.talleres.error;

import com.salesianostriana.dam.talleres.error.notfound.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Recurso no encontrado");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://talleres-api.com/errors/not-found"));
        return problem;
    }

    @ExceptionHandler(OrganizadorConTalleresException.class)
    public ProblemDetail handleOrganizadorConTalleres(OrganizadorConTalleresException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Organizador con talleres");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://talleres-api.com/errors/organizador-con-talleres"));
        return problem;
    }

    @ExceptionHandler(TallerYaExisteException.class)
    public ProblemDetail handleTallerYaExiste(TallerYaExisteException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Taller ya existe");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://talleres-api.com/errors/taller-ya-existe"));
        return problem;
    }

    @ExceptionHandler(ParticipanteYaInscritoException.class)
    public ProblemDetail handleParticipanteYaInscrito(ParticipanteYaInscritoException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Participante ya inscrito");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://talleres-api.com/errors/participante-ya-inscrito"));
        return problem;
    }

}
