package com.turismo.plataforma.dto.cupon;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CuponRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 100, message = "El título admite máximo 100 caracteres")
        String titulo,

        @Size(max = 255, message = "La descripción admite máximo 255 caracteres")
        String descripcion,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin,

        @NotNull(message = "El porcentaje de descuento es obligatorio")
        @DecimalMin(value = "0.01", message = "El descuento debe ser mayor que 0")
        @DecimalMax(value = "100.00", message = "El descuento no puede superar el 100%")
        @Digits(integer = 3, fraction = 2, message = "El descuento admite máximo 2 decimales")
        BigDecimal porcentajeDescuento) {
}