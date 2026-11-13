package com.turismo.plataforma.dto.plan;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PlanRequest(
        @NotBlank(message = "El nombre del plan es obligatorio")
        @Size(max = 30, message = "Máximo 30 caracteres")
        String nombrePlan,

        @Size(max = 200, message = "Máximo 200 caracteres")
        String descripcionPlan,

        @NotNull(message = "El peso máximo de equipaje es obligatorio")
        @PositiveOrZero(message = "El peso no puede ser negativo")
        @Digits(integer = 3, fraction = 2, message = "El peso admite hasta 999.99 kg")
        BigDecimal pesoMaxEquipaje,

        @NotNull(message = "Indica si incluye check-in")
        Boolean incluyeCheckIn,

        @NotNull(message = "El precio extra es obligatorio")
        @PositiveOrZero(message = "El precio extra no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El precio admite máximo 2 decimales")
        BigDecimal precioExtra) {
}
