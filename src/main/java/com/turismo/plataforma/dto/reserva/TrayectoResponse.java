package com.turismo.plataforma.dto.reserva;

import com.turismo.plataforma.model.ProgramacionViaje;
import com.turismo.plataforma.model.ReservaTrayecto;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.model.enums.TipoTrayecto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Un trayecto de la reserva con los datos de su viaje. */
public record TrayectoResponse(Integer ordenTrayecto, TipoTrayecto tipoTrayecto, Long idProgramacion,
                               String direccionOrigen, String direccionDestino,
                               LocalDateTime fechaHoraSalida, String codigoVehiculo, BigDecimal precioBase) {

    public static TrayectoResponse from(ReservaTrayecto trayecto) {
        ProgramacionViaje programacion = trayecto.getProgramacion();
        Ruta ruta = programacion.getRuta();
        return new TrayectoResponse(trayecto.getOrdenTrayecto(), trayecto.getTipoTrayecto(),
                programacion.getIdProgramacion(), ruta.getDireccionOrigen(), ruta.getDireccionDestino(),
                programacion.getFechaHoraSalida(), programacion.getVehiculo().getCodigoIdentificacion(),
                ruta.getPrecioBase());
    }
}