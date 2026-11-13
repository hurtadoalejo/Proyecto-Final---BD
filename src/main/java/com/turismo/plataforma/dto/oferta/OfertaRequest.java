package com.turismo.plataforma.dto.oferta;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 30, message = "Máximo 30 caracteres")
        String titulo,

        @Size(max = 100, message = "Máximo 100 caracteres")
        String descripcion,

        @NotNull(message = "El porcentaje de descuento es obligatorio")
        @DecimalMin(value = "0.01", message = "El descuento debe ser mayor que 0")
        @DecimalMax(value = "100.00", message = "El descuento no puede superar el 100%")
        @Digits(integer = 3, fraction = 2, message = "El descuento admite máximo 2 decimales")
        BigDecimal porcentajeDescuento,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin) {
}
