package com.turismo.plataforma.dto.cliente;

import java.math.BigDecimal;

/**
 * Cliente con lo que ha gastado (para la gestión de clientes).
 * totalGastado y comprasPagadas solo cuentan transacciones APROBADAS.
 */
public record ClienteResumenResponse(ClienteResponse datosPersonales,
                                     BigDecimal totalGastado,
                                     long comprasPagadas,
                                     long reservas) {
}
