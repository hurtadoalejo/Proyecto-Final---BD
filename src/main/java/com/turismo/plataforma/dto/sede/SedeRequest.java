package com.turismo.plataforma.dto.sede;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Sede nueva o modificada. pais, departamento y ciudad van por nombre, como en GET /api/catalogos/ubicaciones. */
public record SedeRequest(
        @NotBlank(message = "El nombre de la sede es obligatorio")
        @Size(max = 40, message = "Máximo 40 caracteres")
        String nombreSede,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 60, message = "Máximo 60 caracteres")
        String direccion,

        @NotBlank(message = "El país es obligatorio")
        String pais,

        @NotBlank(message = "El departamento es obligatorio")
        String departamento,

        @NotBlank(message = "La ciudad es obligatoria")
        String ciudad) {
}
