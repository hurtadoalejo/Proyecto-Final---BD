package com.turismo.plataforma.dto.programacion;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** Una salida puntual de una ruta. fechaHoraSalida en formato "2026-12-20T08:30:00". */
public record ProgramacionRequest(
        @NotNull(message = "La ruta es obligatoria")
        Long idRuta,

        @NotNull(message = "La fecha y hora de salida es obligatoria")
        @Future(message = "La salida debe ser en el futuro")
        LocalDateTime fechaHoraSalida) {
}
