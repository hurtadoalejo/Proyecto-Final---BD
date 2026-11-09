package com.turismo.plataforma.dto.reserva;

import com.turismo.plataforma.model.enums.TipoTrayecto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Un viaje dentro de la reserva (por ejemplo, la ida). */
public record TrayectoRequest(
        @NotNull(message = "La programación es obligatoria")
        Long idProgramacion,

        @NotNull(message = "El orden es obligatorio")
        @Min(value = 1, message = "El orden empieza en 1")
        Integer ordenTrayecto,

        @NotNull(message = "El tipo es obligatorio (IDA o VUELTA)")
        TipoTrayecto tipoTrayecto) {
}