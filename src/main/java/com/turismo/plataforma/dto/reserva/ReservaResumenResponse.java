package com.turismo.plataforma.dto.reserva;

import com.turismo.plataforma.model.Reserva;

import java.time.LocalDateTime;

/** Fila del listado de reservas (sin calcular valores para que el listado sea liviano). */
public record ReservaResumenResponse(Long idReserva, LocalDateTime fechaReserva, String estadoReserva,
                                     Long idCliente, String nombreCliente, boolean pagada) {

    public static ReservaResumenResponse from(Reserva reserva) {
        return new ReservaResumenResponse(
                reserva.getIdReserva(),
                reserva.getFechaReserva(),
                reserva.getEstadoReserva().getNombreEstadoReserva(),
                reserva.getCliente().getIdUsuario(),
                reserva.getCliente().getNombre().completo(),
                reserva.getTransaccion() != null);
    }
}
