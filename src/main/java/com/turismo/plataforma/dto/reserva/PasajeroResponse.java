package com.turismo.plataforma.dto.reserva;

import com.turismo.plataforma.model.DetalleReserva;
import com.turismo.plataforma.model.NombreCompleto;
import com.turismo.plataforma.model.Plan;

import java.math.BigDecimal;

/** Un pasajero de la reserva con su asiento y su plan. */
public record PasajeroResponse(Long idDetalle, String primerNombre, String segundoNombre, String primerApellido,
                               String segundoApellido, String identificacion, String numeroAsiento,
                               Long idPlan, String nombrePlan, BigDecimal precioExtra) {

    public static PasajeroResponse from(DetalleReserva detalle) {
        NombreCompleto nombre = detalle.getNombre();
        Plan plan = detalle.getPlan();
        return new PasajeroResponse(detalle.getIdDetalle(), nombre.getPrimerNombre(), nombre.getSegundoNombre(),
                nombre.getPrimerApellido(), nombre.getSegundoApellido(), detalle.getIdentificacion(),
                detalle.getNumeroAsiento(), plan.getIdPlan(), plan.getNombrePlan(), plan.getPrecioExtra());
    }
}