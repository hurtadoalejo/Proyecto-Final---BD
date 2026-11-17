package com.turismo.plataforma.dto.reporte;

import java.math.BigDecimal;

/** Lo que ha gastado un cliente (pagos aprobados) frente al promedio de todos los clientes. */
public record ClienteGastoResponse(Long idCliente, String nombreCliente, String correo, long cantidadReservas,
                                   BigDecimal totalGastado, BigDecimal promedioClientes) {
}
