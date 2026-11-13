package com.turismo.plataforma.dto.vehiculo;

import jakarta.validation.constraints.*;

public record VehiculoRequest(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 25, message = "Máximo 25 caracteres")
        String codigoIdentificacion,

        @NotBlank(message = "El tipo de transporte es obligatorio (AVION, BUS o TREN)")
        String tipoTransporte,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad mínima es 1")
        @Max(value = 1000, message = "La capacidad máxima es 1000")
        Integer capacidad) {
}
