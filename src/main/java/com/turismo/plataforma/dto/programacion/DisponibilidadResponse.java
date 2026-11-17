package com.turismo.plataforma.dto.programacion;

import java.util.List;

/**
 * Atributo derivado asientosDisponibles = capacidad del vehículo - pasajeros ya reservados.
 * ocupados: números de asiento ya tomados (para pintar un mapa de sillas si se quiere).
 */
public record DisponibilidadResponse(Long idProgramacionViaje, int capacidad, long asientosOcupados,
                                     long asientosDisponibles, List<String> ocupados) {
}
