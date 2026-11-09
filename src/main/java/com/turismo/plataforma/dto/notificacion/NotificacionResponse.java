package com.turismo.plataforma.dto.notificacion;

import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Cupon;
import com.turismo.plataforma.model.Notificacion;
import com.turismo.plataforma.model.enums.EstadoNotificacion;

import java.time.LocalDateTime;

public record NotificacionResponse(Long idNotificacion, String mensaje, EstadoNotificacion estadoNotificacion,
                                   LocalDateTime fechaEnvio, Long idCliente, String nombreCliente,
                                   String correoCliente, Long idCupon, String tituloCupon) {

    public static NotificacionResponse from(Notificacion notificacion) {
        Cliente cliente = notificacion.getCliente();
        Cupon cupon = notificacion.getCupon();
        return new NotificacionResponse(
                notificacion.getIdNotificacion(),
                notificacion.getMensaje(),
                notificacion.getEstadoNotificacion(),
                notificacion.getFechaEnvio(),
                cliente.getIdUsuario(),
                cliente.getNombre().getPrimerNombre() + " " + cliente.getNombre().getPrimerApellido(),
                cliente.getCorreo(),
                cupon == null ? null : cupon.getIdCupon(),
                cupon == null ? null : cupon.getTitulo());
    }
}