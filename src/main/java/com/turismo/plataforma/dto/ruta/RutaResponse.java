package com.turismo.plataforma.dto.ruta;

import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.model.Vehiculo;

import java.math.BigDecimal;

public record RutaResponse(Long idRuta, Long idCiudadOrigen, String ciudadOrigen, String codigoOrigen,
                           String direccionOrigen, Long idCiudadDestino, String ciudadDestino, String codigoDestino,
                           String direccionDestino, BigDecimal distancia, Integer duracionEstimada,
                           BigDecimal precioBase, Long idVehiculo, String codigoVehiculo, String tipoTransporte) {

    public static RutaResponse from(Ruta ruta) {
        Vehiculo vehiculo = ruta.getVehiculo();
        return new RutaResponse(
                ruta.getIdRuta(),
                ruta.getCiudadOrigen().getIdCiudad(),
                ruta.getCiudadOrigen().getNombreCiudad(),
                ruta.getCiudadOrigen().getCodigoCiudad(),
                ruta.getDireccionOrigen(),
                ruta.getCiudadDestino().getIdCiudad(),
                ruta.getCiudadDestino().getNombreCiudad(),
                ruta.getCiudadDestino().getCodigoCiudad(),
                ruta.getDireccionDestino(),
                ruta.getDistancia(),
                ruta.getDuracionEstimada(),
                ruta.getPrecioBase(),
                vehiculo == null ? null : vehiculo.getIdVehiculo(),
                vehiculo == null ? null : vehiculo.getCodigoIdentificacion(),
                vehiculo == null ? null : vehiculo.getTipoTransporte().getNombreTipoTransporte());
    }
}
