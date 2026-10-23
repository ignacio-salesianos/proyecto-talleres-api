package com.salesianostriana.dam.talleres.validation;

import com.salesianostriana.dam.talleres.dto.TallerRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FechaFinPosteriorValidator
        implements ConstraintValidator<FechaFinPosterior, TallerRequestDTO> {

    @Override
    public boolean isValid(TallerRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.fechaHoraInicio() == null || dto.fechaHoraFin() == null) {
            return true;
        }
        return dto.fechaHoraFin().isAfter(dto.fechaHoraInicio());
    }

}
