package com.turismo.plataforma.dto.ruta;

import com.turismo.plataforma.model.Ruta;

import java.math.BigDecimal;

public record RutaResponse(Long idRuta, String direccionOrigen, String direccionDestino,
                           BigDecimal distancia, Integer duracionEstimada, BigDecimal precioBase) {

    public static RutaResponse from(Ruta ruta) {
        return new RutaResponse(ruta.getIdRuta(), ruta.getDireccionOrigen(), ruta.getDireccionDestino(),
                ruta.getDistancia(), ruta.getDuracionEstimada(), ruta.getPrecioBase());
    }
}