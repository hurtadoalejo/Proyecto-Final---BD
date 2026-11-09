package com.turismo.plataforma.dto.notificacion;

import com.turismo.plataforma.model.enums.EstadoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificacionRequest(
        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        @NotBlank(message = "El mensaje es obligatorio")
        @Size(max = 500, message = "El mensaje admite máximo 500 caracteres")
        String mensaje,

        Long idCupon,

        @NotNull(message = "El estado es obligatorio (ENVIADO, FALLIDO o PENDIENTE)")
        EstadoNotificacion estadoNotificacion) {
}