package com.turismo.plataforma.dto.vehiculo;

import com.turismo.plataforma.model.Vehiculo;

public record VehiculoResponse(Long idVehiculo, String codigoIdentificacion, String tipoTransporte, Integer capacidad) {

    public static VehiculoResponse from(Vehiculo vehiculo) {
        return new VehiculoResponse(vehiculo.getIdVehiculo(), vehiculo.getCodigoIdentificacion(),
                vehiculo.getTipoTransporte().getNombreTipoTransporte(), vehiculo.getCapacidad());
    }
}
