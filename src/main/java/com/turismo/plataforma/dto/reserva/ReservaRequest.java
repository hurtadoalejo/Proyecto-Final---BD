package com.turismo.plataforma.dto.reserva;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Nueva reserva. idEmpleado (quien atiende) e idOferta son opcionales.
 * Cada pasajero viaja en todos los viajes de la reserva.
 */
public record ReservaRequest(
        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        Long idEmpleado,

        Long idOferta,

        @NotEmpty(message = "Debe haber al menos un viaje")
        @Size(max = 6, message = "Máximo 6 viajes")
        List<@Valid ViajeReservaRequest> viajes,

        @NotEmpty(message = "Debe haber al menos un pasajero")
        @Size(max = 9, message = "Máximo 9 pasajeros")
        List<@Valid PasajeroRequest> pasajeros) {
}
