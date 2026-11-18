package com.turismo.plataforma.dto.programacion;

import java.util.List;

/**
 * creadas: salidas nuevas · omitidas: cuántas ya existían · conflictos: cuántas no se crearon porque el
 * vehículo no tenía 12 horas de descanso con otro viaje.
 */
public record ProgramacionRecurrenteResponse(int creadas, int omitidas, int conflictos,
                                             List<ProgramacionResponse> salidas) {
}
