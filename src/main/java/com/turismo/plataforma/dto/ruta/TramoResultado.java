package com.turismo.plataforma.dto.ruta;

import java.time.LocalDate;
import java.util.List;

/** Salidas disponibles para un tramo. viajes vacío = no hay salidas con cupo ese día. */
public record TramoResultado(String origen, String ciudadOrigen, String destino, String ciudadDestino,
                             LocalDate fecha, List<ViajeDisponible> viajes) {
}
