package com.turismo.plataforma.dto.programacion;

import com.turismo.plataforma.model.ProgramacionViaje;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.model.Vehiculo;

import java.time.LocalDateTime;

public record ProgramacionResponse(Long idProgramacionViaje, Long idRuta, String codigoOrigen, String ciudadOrigen,
                                   String codigoDestino, String ciudadDestino, LocalDateTime fechaHoraSalida,
                                   LocalDateTime fechaHoraLlegada, String tipoTransporte, String codigoVehiculo) {

    public static ProgramacionResponse from(ProgramacionViaje programacion) {
        Ruta ruta = programacion.getRuta();
        Vehiculo vehiculo = ruta.getVehiculo();
        return new ProgramacionResponse(
                programacion.getIdProgramacionViaje(),
                ruta.getIdRuta(),
                ruta.getCiudadOrigen().getCodigoCiudad(),
                ruta.getCiudadOrigen().getNombreCiudad(),
                ruta.getCiudadDestino().getCodigoCiudad(),
                ruta.getCiudadDestino().getNombreCiudad(),
                programacion.getFechaHoraSalida(),
                programacion.calcularLlegada(),
                vehiculo == null ? null : vehiculo.getTipoTransporte().getNombreTipoTransporte(),
                vehiculo == null ? null : vehiculo.getCodigoIdentificacion());
    }
}
