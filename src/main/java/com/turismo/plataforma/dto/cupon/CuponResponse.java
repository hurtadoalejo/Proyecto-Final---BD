package com.turismo.plataforma.dto.cupon;

import com.turismo.plataforma.model.Cupon;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CuponResponse(Long idCupon, String titulo, String descripcion, LocalDate fechaInicio,
                            LocalDate fechaFin, BigDecimal porcentajeDescuento) {

    public static CuponResponse from(Cupon cupon) {
        return new CuponResponse(cupon.getIdCupon(), cupon.getTitulo(), cupon.getDescripcion(),
                cupon.getFechaInicio(), cupon.getFechaFin(), cupon.getPorcentajeDescuento());
    }
}