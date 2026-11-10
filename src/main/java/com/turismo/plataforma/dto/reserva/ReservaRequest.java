package com.turismo.plataforma.dto.reserva;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/** Una reserva completa: cliente, trayectos y pasajeros en una sola petición. */
public record ReservaRequest(
        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        @NotEmpty(message = "La reserva necesita al menos un trayecto")
        List<@Valid TrayectoRequest> trayectos,

        @NotEmpty(message = "La reserva necesita al menos un pasajero")
        List<@Valid PasajeroRequest> pasajeros) {
}