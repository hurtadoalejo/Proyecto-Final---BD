package com.turismo.plataforma.dto.ruta;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Un tramo de la búsqueda: nombres de ciudad (Madrid, París...) y fecha de salida. */
public record TramoRequest(
        @NotBlank(message = "El origen es obligatorio")
        @Size(max = 40, message = "Máximo 40 caracteres")
        String origen,

        @NotBlank(message = "El destino es obligatorio")
        @Size(max = 40, message = "Máximo 40 caracteres")
        String destino,

        @NotNull(message = "La fecha es obligatoria")
        @FutureOrPresent(message = "La fecha no puede estar en el pasado")
        LocalDate fecha) {
}
