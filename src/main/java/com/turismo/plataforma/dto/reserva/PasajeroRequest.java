package com.turismo.plataforma.dto.reserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Un pasajero de la reserva, con su asiento y su plan. */
public record PasajeroRequest(
        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 50, message = "Máximo 50 caracteres")
        String primerNombre,

        @Size(max = 50, message = "Máximo 50 caracteres")
        String segundoNombre,

        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 50, message = "Máximo 50 caracteres")
        String primerApellido,

        @Size(max = 50, message = "Máximo 50 caracteres")
        String segundoApellido,

        @NotBlank(message = "La identificación es obligatoria")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String identificacion,

        @NotBlank(message = "El asiento es obligatorio")
        @Size(max = 5, message = "El asiento admite máximo 5 caracteres")
        String numeroAsiento,

        @NotNull(message = "El plan es obligatorio")
        Long idPlan) {
}