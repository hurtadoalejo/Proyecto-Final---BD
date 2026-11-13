package com.turismo.plataforma.dto.ruta;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/** Ruta con las columnas del modelo, más el vehículo que la cubre (tabla RutaVehiculo). */
public record RutaRequest(
        @NotNull(message = "La ciudad de origen es obligatoria")
        Long idCiudadOrigen,

        @Size(max = 40, message = "Máximo 40 caracteres")
        String direccionOrigen,

        @NotNull(message = "La ciudad de destino es obligatoria")
        Long idCiudadDestino,

        @Size(max = 40, message = "Máximo 40 caracteres")
        String direccionDestino,

        @NotNull(message = "La distancia es obligatoria")
        @Positive(message = "La distancia debe ser mayor que 0")
        @Digits(integer = 8, fraction = 2, message = "La distancia admite máximo 2 decimales")
        BigDecimal distancia,

        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración debe ser mayor que 0 minutos")
        Integer duracionEstimada,

        @NotNull(message = "El precio base es obligatorio")
        @PositiveOrZero(message = "El precio base no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El precio admite máximo 2 decimales")
        BigDecimal precioBase,

        @NotNull(message = "El vehículo es obligatorio")
        Long idVehiculo) {
}
