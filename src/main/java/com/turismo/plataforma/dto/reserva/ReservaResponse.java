package com.turismo.plataforma.dto.reserva;

import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Reserva;
import com.turismo.plataforma.model.enums.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Detalle completo de una reserva: trayectos, pasajeros y valor total. */
public record ReservaResponse(Long idReserva, LocalDateTime fechaReserva, EstadoReserva estadoReserva,
                              Long idCliente, String nombreCliente, List<TrayectoResponse> trayectos,
                              List<PasajeroResponse> pasajeros, BigDecimal valorTotal) {

    public static ReservaResponse from(Reserva reserva) {
        Cliente cliente = reserva.getCliente();
        return new ReservaResponse(
                reserva.getIdReserva(),
                reserva.getFechaReserva(),
                reserva.getEstadoReserva(),
                cliente.getIdUsuario(),
                cliente.getNombre().getPrimerNombre() + " " + cliente.getNombre().getPrimerApellido(),
                reserva.getTrayectos().stream().map(TrayectoResponse::from).toList(),
                reserva.getDetalles().stream().map(PasajeroResponse::from).toList(),
                reserva.calcularValorTotal());
    }
}