package com.turismo.plataforma.dto.ruta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/** Búsqueda del frontend: tipo de transporte, cantidad de pasajeros y tramos (ida, vuelta...). */
public record BusquedaRutasRequest(
        @NotBlank(message = "El tipo de transporte es obligatorio (AVION, BUS o TREN)")
        String tipoTransporte,

        @NotNull(message = "La cantidad de pasajeros es obligatoria")
        @Min(value = 1, message = "Debe haber al menos 1 pasajero")
        @Max(value = 9, message = "Máximo 9 pasajeros por búsqueda")
        Integer pasajeros,

        @NotEmpty(message = "Debe haber al menos un tramo")
        @Size(max = 6, message = "Máximo 6 tramos")
        List<@Valid TramoRequest> tramos) {
}
