package com.turismo.plataforma.dto.programacion;

import com.turismo.plataforma.model.ProgramacionViaje;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.model.Vehiculo;
import com.turismo.plataforma.model.enums.TipoTransporte;

import java.time.LocalDateTime;

public record ProgramacionViajeResponse(
        Long idProgramacion,
        LocalDateTime fechaHoraSalida,
        Long idRuta,
        String direccionOrigen,
        String direccionDestino,
        Long idVehiculo,
        String codigoVehiculo,
        TipoTransporte tipoTransporte) {

    public static ProgramacionViajeResponse from(ProgramacionViaje programacion) {
        Ruta ruta = programacion.getRuta();
        Vehiculo vehiculo = programacion.getVehiculo();
        return new ProgramacionViajeResponse(
                programacion.getIdProgramacion(),
                programacion.getFechaHoraSalida(),
                ruta.getIdRuta(),
                ruta.getDireccionOrigen(),
                ruta.getDireccionDestino(),
                vehiculo.getIdVehiculo(),
                vehiculo.getCodigoIdentificacion(),
                vehiculo.getTipoTransporte());
    }
}