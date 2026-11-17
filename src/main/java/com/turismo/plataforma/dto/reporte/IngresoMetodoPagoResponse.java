package com.turismo.plataforma.dto.reporte;

import java.math.BigDecimal;

/** Ingresos aprobados de un método de pago y su porcentaje sobre el total. */
public record IngresoMetodoPagoResponse(String metodoPago, long cantidadPagos, BigDecimal totalIngresos,
                                        BigDecimal porcentaje) {
}
