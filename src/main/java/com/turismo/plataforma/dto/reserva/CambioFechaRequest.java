package com.turismo.plataforma.dto.reserva;

import jakarta.validation.constraints.NotNull;

/** Nueva salida (de la misma ruta) a la que se mueve un viaje de la reserva. */
public record CambioFechaRequest(
        @NotNull(message = "Elige la nueva salida") Long idProgramacionViajeNuevo) {
}
