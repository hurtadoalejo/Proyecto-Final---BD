package com.turismo.plataforma.dto.programacion;

/** Atributo derivado asientosDisponibles de un viaje programado. */
public record DisponibilidadResponse(Long idProgramacion, Integer capacidad, long asientosOcupados,
                                     long asientosDisponibles) {
}