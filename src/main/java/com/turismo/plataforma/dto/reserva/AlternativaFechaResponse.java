package com.turismo.plataforma.dto.reserva;

import java.time.LocalDateTime;

/** Otra salida de la misma ruta a la que el cliente puede mover un viaje de su reserva. */
public record AlternativaFechaResponse(Long idProgramacionViaje, LocalDateTime fechaHoraSalida,
                                       LocalDateTime fechaHoraLlegada, long asientosDisponibles) {
}
