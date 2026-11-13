package com.turismo.plataforma.dto.notificacion;

import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Notificacion;

import java.time.LocalDateTime;
import java.util.List;

public record NotificacionResponse(Long idNotificacion, String mensaje, LocalDateTime fechaEnvio,
                                   String estadoNotificacion, Long idOferta, String tituloOferta,
                                   List<Long> idsClientes) {

    public static NotificacionResponse from(Notificacion notificacion) {
        return new NotificacionResponse(
                notificacion.getIdNotificacion(),
                notificacion.getMensaje(),
                notificacion.getFechaEnvio(),
                notificacion.getEstadoNotificacion().getNombreEstadoNotificacion(),
                notificacion.getOferta() == null ? null : notificacion.getOferta().getIdOferta(),
                notificacion.getOferta() == null ? null : notificacion.getOferta().getTitulo(),
                notificacion.getClientes().stream().map(Cliente::getIdUsuario).sorted().toList());
    }
}
