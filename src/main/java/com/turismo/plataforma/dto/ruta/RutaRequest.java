package com.turismo.plataforma.dto.ruta;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RutaRequest(
        @NotBlank(message = "El origen es obligatorio")
        @Size(max = 150, message = "El origen admite máximo 150 caracteres")
        String direccionOrigen,

        @NotBlank(message = "El destino es obligatorio")
        @Size(max = 150, message = "El destino admite máximo 150 caracteres")
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
        BigDecimal precioBase) {
}