package com.salesianostriana.dam.talleres.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.talleres.error.notfound.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Datos no válidos");
        problem.setDetail("Error de validación");
        problem.setType(URI.create("https://talleres-api.com/errors/validation"));

        List<ApiValidationSubError> subErrors = ex.getConstraintViolations().stream()
                .map(ApiValidationSubError::from)
                .toList();

        problem.setProperty("invalid-params", subErrors);
        return problem;
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Datos no válidos");
        problem.setDetail("Error de validación");
        problem.setType(URI.create("https://talleres-api.com/errors/validation"));

        List<ApiValidationSubError> subErrors = ex.getAllErrors().stream()
                .map(ApiValidationSubError::from)
                .toList();

        problem.setProperty("invalid-params", subErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @Builder
    record ApiValidationSubError(
            String object,
            String message,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String field,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Object rejectedValue
    ) {

        public static ApiValidationSubError from(ObjectError error) {
            if (error instanceof FieldError fieldError) {
                return ApiValidationSubError.builder()
                        .object(fieldError.getObjectName())
                        .message(fieldError.getDefaultMessage())
                        .field(fieldError.getField())
                        .rejectedValue(fieldError.getRejectedValue())
                        .build();
            }
            return ApiValidationSubError.builder()
                    .object(error.getObjectName())
                    .message(error.getDefaultMessage())
                    .build();
        }

        public static ApiValidationSubError from(ConstraintViolation<?> v) {
            return ApiValidationSubError.builder()
                    .object(v.getRootBeanClass().getSimpleName())
                    .message(v.getMessage())
                    .field(getFieldName(v))
                    .rejectedValue(v.getInvalidValue())
                    .build();
        }

        private static String getFieldName(ConstraintViolation<?> v) {
            String field = null;
            for (Path.Node node : v.getPropertyPath()) {
                if (node.getKind() == ElementKind.PROPERTY || node.getKind() == ElementKind.PARAMETER) {
                    field = node.getName();
                }
            }
            return field;
        }
    }

}
