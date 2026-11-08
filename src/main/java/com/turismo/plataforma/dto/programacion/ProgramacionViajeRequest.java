package com.turismo.plataforma.dto.programacion;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ProgramacionViajeRequest(
        @NotNull(message = "La ruta es obligatoria")
        Long idRuta,

        @NotNull(message = "El vehículo es obligatorio")
        Long idVehiculo,

        @NotNull(message = "La fecha y hora de salida es obligatoria")
        @Future(message = "La salida debe ser en el futuro")
        LocalDateTime fechaHoraSalida) {
}