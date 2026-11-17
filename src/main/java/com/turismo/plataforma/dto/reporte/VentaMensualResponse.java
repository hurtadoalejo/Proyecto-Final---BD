package com.turismo.plataforma.dto.reporte;

import java.math.BigDecimal;

/** Ventas aprobadas de un mes. periodo = "2026-11". */
public record VentaMensualResponse(String periodo, long cantidadVentas, BigDecimal totalVendido) {
}
