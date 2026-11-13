package com.turismo.plataforma.dto.reserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Un pasajero de la reserva (tabla DetalleReserva). */
public record PasajeroRequest(
        @NotBlank(message = "El tipo de identificación es obligatorio")
        String tipoIdentificacion,

        @NotBlank(message = "El número de identificación es obligatorio")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String numeroIdentificacion,

        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String primerNombre,

        @Size(max = 20, message = "Máximo 20 caracteres")
        String segundoNombre,

        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String primerApellido,

        @Size(max = 20, message = "Máximo 20 caracteres")
        String segundoApellido,

        @NotBlank(message = "El número de asiento es obligatorio")
        @Pattern(regexp = "^[A-Za-z0-9-]{1,10}$", message = "Asiento inválido (ej.: 12A)")
        String numeroAsiento,

        @NotNull(message = "El plan es obligatorio")
        Long idPlan) {
}
