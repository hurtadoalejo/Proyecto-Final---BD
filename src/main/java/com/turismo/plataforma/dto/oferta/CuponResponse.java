package com.turismo.plataforma.dto.oferta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Oferta vigente que el cliente todavía puede usar en una reserva (fechaNotificacion: cuándo se le avisó, o null). */
public record CuponResponse(Long idOferta, String titulo, String descripcion, BigDecimal porcentajeDescuento,
                            LocalDate fechaInicio, LocalDate fechaFin, LocalDateTime fechaNotificacion) {
}
