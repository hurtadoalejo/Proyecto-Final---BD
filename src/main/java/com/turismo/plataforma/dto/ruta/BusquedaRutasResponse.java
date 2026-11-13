package com.turismo.plataforma.dto.ruta;

import java.util.List;

/** Resultado de la búsqueda: un bloque por cada tramo pedido, en el mismo orden. */
public record BusquedaRutasResponse(String tipoTransporte, Integer pasajeros, List<TramoResultado> tramos) {
}
