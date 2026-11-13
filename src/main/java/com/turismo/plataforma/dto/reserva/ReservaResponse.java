package com.turismo.plataforma.dto.reserva;

import com.turismo.plataforma.dto.pago.PagoResponse;
import com.turismo.plataforma.model.DetalleReserva;
import com.turismo.plataforma.model.Reserva;
import com.turismo.plataforma.model.ReservaViaje;
import com.turismo.plataforma.model.Ruta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Reserva completa con sus viajes, pasajeros, valores calculados (atributos derivados)
 * y el pago (null mientras no se haya pagado).
 */
public record ReservaResponse(Long idReserva, LocalDateTime fechaReserva, String estadoReserva,
                              Long idCliente, String nombreCliente, Long idEmpleado, String nombreEmpleado,
                              Long idOferta, String tituloOferta, List<Viaje> viajes, List<Pasajero> pasajeros,
                              BigDecimal subtotal, BigDecimal descuento, BigDecimal valorTotal,
                              PagoResponse pago) {

    public record Viaje(Long idProgramacionViaje, String tipoTrayecto, String codigoOrigen, String ciudadOrigen,
                        String codigoDestino, String ciudadDestino, LocalDateTime fechaHoraSalida,
                        LocalDateTime fechaHoraLlegada, BigDecimal precioBase) {

        static Viaje from(ReservaViaje viaje) {
            Ruta ruta = viaje.getProgramacionViaje().getRuta();
            return new Viaje(
                    viaje.getProgramacionViaje().getIdProgramacionViaje(),
                    viaje.getTipoTrayecto().getNombreTipoTrayecto(),
                    ruta.getCiudadOrigen().getCodigoCiudad(),
                    ruta.getCiudadOrigen().getNombreCiudad(),
                    ruta.getCiudadDestino().getCodigoCiudad(),
                    ruta.getCiudadDestino().getNombreCiudad(),
                    viaje.getProgramacionViaje().getFechaHoraSalida(),
                    viaje.getProgramacionViaje().calcularLlegada(),
                    ruta.getPrecioBase());
        }
    }

    public record Pasajero(Long idDetalle, String tipoIdentificacion, String numeroIdentificacion,
                           String nombreCompleto, String numeroAsiento, Long idPlan, String nombrePlan,
                           BigDecimal precioExtra) {

        static Pasajero from(DetalleReserva detalle) {
            return new Pasajero(
                    detalle.getId().getIdDetalle(),
                    detalle.getTipoIdentificacion().getNombreIdentificacion(),
                    detalle.getIdentificacion(),
                    detalle.getNombre().completo(),
                    detalle.getNumeroAsiento(),
                    detalle.getPlan().getIdPlan(),
                    detalle.getPlan().getNombrePlan(),
                    detalle.getPlan().getPrecioExtra());
        }
    }

    public static ReservaResponse from(Reserva reserva) {
        return new ReservaResponse(
                reserva.getIdReserva(),
                reserva.getFechaReserva(),
                reserva.getEstadoReserva().getNombreEstadoReserva(),
                reserva.getCliente().getIdUsuario(),
                reserva.getCliente().getNombre().completo(),
                reserva.getEmpleado() == null ? null : reserva.getEmpleado().getIdUsuario(),
                reserva.getEmpleado() == null ? null : reserva.getEmpleado().getNombre().completo(),
                reserva.getOferta() == null ? null : reserva.getOferta().getIdOferta(),
                reserva.getOferta() == null ? null : reserva.getOferta().getTitulo(),
                reserva.getViajes().stream().map(Viaje::from).toList(),
                reserva.getDetalles().stream().map(Pasajero::from).toList(),
                reserva.calcularSubtotal(),
                reserva.calcularDescuento(),
                reserva.calcularValorTotal(),
                reserva.getTransaccion() == null ? null : PagoResponse.from(reserva.getTransaccion()));
    }
}
