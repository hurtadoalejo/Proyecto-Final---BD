package com.turismo.plataforma.dto.reserva;

import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Reserva;
import com.turismo.plataforma.model.enums.EstadoReserva;

import java.time.LocalDateTime;

/** Una reserva en el listado: sin trayectos ni pasajeros, para que la lista sea liviana. */
public record ReservaResumenResponse(Long idReserva, LocalDateTime fechaReserva, EstadoReserva estadoReserva,
                                     Long idCliente, String nombreCliente) {

    public static ReservaResumenResponse from(Reserva reserva) {
        Cliente cliente = reserva.getCliente();
        return new ReservaResumenResponse(reserva.getIdReserva(), reserva.getFechaReserva(),
                reserva.getEstadoReserva(), cliente.getIdUsuario(),
                cliente.getNombre().getPrimerNombre() + " " + cliente.getNombre().getPrimerApellido());
    }
}