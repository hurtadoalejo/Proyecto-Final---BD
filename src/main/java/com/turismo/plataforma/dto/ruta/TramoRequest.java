package com.turismo.plataforma.dto.ruta;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Un tramo de la búsqueda: códigos de ciudad (BOG, MAD...) y fecha de salida. */
public record TramoRequest(
        @NotBlank(message = "El origen es obligatorio")
        @Size(min = 3, max = 3, message = "El origen es un código de 3 letras")
        String origen,

        @NotBlank(message = "El destino es obligatorio")
        @Size(min = 3, max = 3, message = "El destino es un código de 3 letras")
        String destino,

        @NotNull(message = "La fecha es obligatoria")
        @FutureOrPresent(message = "La fecha no puede estar en el pasado")
        LocalDate fecha) {
}
