package com.turismo.plataforma.dto.reporte;

import java.math.BigDecimal;

/**
 * Pasajeros (detalles de reserva) de una ruta, sin contar reservas canceladas.
 * promedioCategoria = promedio de pasajeros de las rutas del mismo tipo de transporte (null si no aplica).
 */
public record RutaPasajerosResponse(Long idRuta, String origen, String destino, String tipoTransporte,
                                    long reservas, long pasajeros, BigDecimal promedioCategoria) {
}
