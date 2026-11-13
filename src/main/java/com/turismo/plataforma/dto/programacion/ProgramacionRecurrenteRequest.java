package com.turismo.plataforma.dto.programacion;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Repite una ruta cada cierto tiempo: crea una salida en fechaHoraInicio y otra cada "cadaHoras"
 * horas hasta fechaHoraFin (incluida). Ej.: cadaHoras = 24 es una salida diaria a la misma hora;
 * 168 es semanal. Las salidas que ya existían se omiten.
 */
public record ProgramacionRecurrenteRequest(
        @NotNull(message = "La ruta es obligatoria")
        Long idRuta,

        @NotNull(message = "La fecha y hora de la primera salida es obligatoria")
        @Future(message = "La primera salida debe ser en el futuro")
        LocalDateTime fechaHoraInicio,

        @NotNull(message = "La fecha y hora límite es obligatoria")
        LocalDateTime fechaHoraFin,

        @NotNull(message = "La frecuencia en horas es obligatoria")
        @Min(value = 1, message = "La frecuencia mínima es 1 hora")
        @Max(value = 720, message = "La frecuencia máxima es 720 horas (30 días)")
        Integer cadaHoras) {
}
