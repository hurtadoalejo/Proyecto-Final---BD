package com.turismo.plataforma.dto.programacion;

/** Atributo derivado asientosDisponibles = capacidad del vehículo - pasajeros ya reservados. */
public record DisponibilidadResponse(Long idProgramacionViaje, int capacidad, long asientosOcupados,
                                     long asientosDisponibles) {
}
