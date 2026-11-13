package com.turismo.plataforma.dto.reserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Una salida elegida en la búsqueda. tipoTrayecto: IDA o VUELTA. */
public record ViajeReservaRequest(
        @NotNull(message = "La programación del viaje es obligatoria")
        Long idProgramacionViaje,

        @NotBlank(message = "El tipo de trayecto es obligatorio (IDA o VUELTA)")
        String tipoTrayecto) {
}
