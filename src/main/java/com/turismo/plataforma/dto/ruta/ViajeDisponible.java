package com.turismo.plataforma.dto.ruta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Una salida que se puede reservar. idProgramacionViaje es lo que se envía al crear la reserva.
 * precioTotal = precioBase x pasajeros (sin plan ni oferta).
 */
public record ViajeDisponible(Long idProgramacionViaje, Long idRuta, LocalDateTime fechaHoraSalida,
                              LocalDateTime fechaHoraLlegada, Integer duracionEstimada,
                              String direccionOrigen, String direccionDestino, String tipoTransporte,
                              String codigoVehiculo, BigDecimal distancia, BigDecimal precioBase,
                              BigDecimal precioTotal, long asientosDisponibles) {
}
