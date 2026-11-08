package com.turismo.plataforma.dto.vehiculo;

import com.turismo.plataforma.model.Vehiculo;
import com.turismo.plataforma.model.enums.TipoTransporte;

public record VehiculoResponse(Long idVehiculo, String codigoIdentificacion,
                               TipoTransporte tipoTransporte, Integer capacidad) {

    public static VehiculoResponse from(Vehiculo vehiculo) {
        return new VehiculoResponse(vehiculo.getIdVehiculo(), vehiculo.getCodigoIdentificacion(),
                vehiculo.getTipoTransporte(), vehiculo.getCapacidad());
    }
}