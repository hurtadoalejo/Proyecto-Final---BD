package com.turismo.plataforma.dto.sede;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SedeRequest(
        @NotBlank(message = "El nombre de la sede es obligatorio")
        @Size(max = 40, message = "Máximo 40 caracteres")
        String nombreSede,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 60, message = "Máximo 60 caracteres")
        String direccion,

        @NotNull(message = "La ciudad es obligatoria")
        Long idCiudad) {
}
