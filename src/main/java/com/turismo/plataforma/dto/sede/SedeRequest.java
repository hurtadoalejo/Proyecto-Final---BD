package com.turismo.plataforma.dto.sede;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SedeRequest(
        @NotBlank(message = "El nombre de la sede es obligatorio")
        @Size(max = 100, message = "El nombre admite máximo 100 caracteres")
        String nombreSede,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 150, message = "La dirección admite máximo 150 caracteres")
        String direccion) {
}