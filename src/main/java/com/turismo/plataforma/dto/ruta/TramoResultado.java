package com.turismo.plataforma.dto.ruta;

import java.time.LocalDate;
import java.util.List;

/** Salidas disponibles para un tramo. viajes vacío = no hay salidas con cupo ese día. */
public record TramoResultado(String origen, String paisOrigen, String destino, String paisDestino,
                             LocalDate fecha, List<ViajeDisponible> viajes) {
}
