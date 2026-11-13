package com.turismo.plataforma.dto.pago;

import com.turismo.plataforma.model.Factura;
import com.turismo.plataforma.model.Transaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Transacción de la reserva y su factura. numeroFactura = "FV-" + idReserva con 6 dígitos. */
public record PagoResponse(Long idReserva, BigDecimal montoPagado, LocalDateTime fechaPago, String metodoPago,
                           String estadoTransaccion, String numeroFactura, LocalDateTime fechaEmision) {

    public static PagoResponse from(Transaccion transaccion) {
        Factura factura = transaccion.getFactura();
        return new PagoResponse(
                transaccion.getIdReserva(),
                transaccion.getMontoPagado(),
                transaccion.getFechaPago(),
                transaccion.getMetodoPago().getNombreMetodoPago(),
                transaccion.getEstadoTransaccion().getNombreEstadoTransaccion(),
                factura == null ? null : String.format("FV-%06d", factura.getIdReserva()),
                factura == null ? null : factura.getFechaEmision());
    }
}
