package com.turismo.plataforma.dto.vehiculo;

import com.turismo.plataforma.model.enums.TipoTransporte;
import jakarta.validation.constraints.*;

public record VehiculoRequest(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 20, message = "El código admite máximo 20 caracteres")
        String codigoIdentificacion,

        @NotNull(message = "El tipo de transporte es obligatorio")
        TipoTransporte tipoTransporte,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad mínima es 1")
        @Max(value = 1000, message = "La capacidad máxima es 1000")
        Integer capacidad) {
}