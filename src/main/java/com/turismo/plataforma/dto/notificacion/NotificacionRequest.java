package com.turismo.plataforma.dto.notificacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Notificación para uno o varios clientes (tabla ClienteNotificacion).
 * estadoNotificacion: PENDIENTE, ENVIADO o FALLIDO. idOferta es opcional.
 */
public record NotificacionRequest(
        @NotBlank(message = "El mensaje es obligatorio")
        @Size(max = 200, message = "Máximo 200 caracteres")
        String mensaje,

        @NotBlank(message = "El estado es obligatorio (PENDIENTE, ENVIADO o FALLIDO)")
        String estadoNotificacion,

        Long idOferta,

        @NotEmpty(message = "Debe haber al menos un cliente")
        Set<@NotNull Long> idsClientes) {
}
