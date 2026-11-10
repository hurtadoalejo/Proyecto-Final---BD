package com.turismo.plataforma.dto.pago;

import com.turismo.plataforma.model.Factura;
import com.turismo.plataforma.model.Transaccion;
import com.turismo.plataforma.model.enums.EstadoTransaccion;
import com.turismo.plataforma.model.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** El pago de una reserva con los datos de su factura. */
public record PagoResponse(Long idReserva, String numeroFactura, BigDecimal montoPagado, MetodoPago metodoPago,
                           EstadoTransaccion estadoTransaccion, LocalDateTime fechaPago,
                           LocalDateTime fechaEmisionFactura) {

    public static PagoResponse from(Transaccion transaccion) {
        Factura factura = transaccion.getFactura();
        return new PagoResponse(
                transaccion.getIdReserva(),
                String.format("FV-%06d", transaccion.getIdReserva()),
                transaccion.getMontoPagado(),
                transaccion.getMetodoPago(),
                transaccion.getEstadoTransaccion(),
                transaccion.getFechaPago(),
                factura == null ? null : factura.getFechaEmision());
    }
}