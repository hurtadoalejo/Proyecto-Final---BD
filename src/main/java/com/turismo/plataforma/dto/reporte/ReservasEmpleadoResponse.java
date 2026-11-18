package com.turismo.plataforma.dto.reporte;

import java.math.BigDecimal;

/** Reservas en las que participó un empleado y lo vendido (pagos aprobados). */
public record ReservasEmpleadoResponse(Long idEmpleado, String nombreEmpleado, String cargo, String sede,
                                       long cantidadReservas, BigDecimal totalVendido) {
}
