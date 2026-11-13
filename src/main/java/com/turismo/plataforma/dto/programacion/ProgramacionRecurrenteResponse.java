package com.turismo.plataforma.dto.programacion;

import java.util.List;

/** creadas: salidas nuevas; omitidas: cuántas ya existían. */
public record ProgramacionRecurrenteResponse(int creadas, int omitidas, List<ProgramacionResponse> salidas) {
}
