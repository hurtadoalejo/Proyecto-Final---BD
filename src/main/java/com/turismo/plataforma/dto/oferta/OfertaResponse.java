package com.turismo.plataforma.dto.oferta;

import com.turismo.plataforma.model.Oferta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaResponse(Long idOferta, String titulo, String descripcion, BigDecimal porcentajeDescuento,
                             LocalDate fechaInicio, LocalDate fechaFin) {

    public static OfertaResponse from(Oferta oferta) {
        return new OfertaResponse(oferta.getIdOferta(), oferta.getTitulo(), oferta.getDescripcion(),
                oferta.getPorcentajeDescuento(), oferta.getFechaInicio(), oferta.getFechaFin());
    }
}
