package com.turismo.plataforma.dto.oferta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Oferta vigente que le llegó al cliente por notificación (se usa como cupón al reservar). */
public record CuponResponse(Long idOferta, String titulo, String descripcion, BigDecimal porcentajeDescuento,
                            LocalDate fechaInicio, LocalDate fechaFin, LocalDateTime fechaNotificacion) {
}
