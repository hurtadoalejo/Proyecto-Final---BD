package com.turismo.plataforma.dto.empleado;

import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.model.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Datos personales (mismo formato que un cliente) más los datos laborales. */
public record EmpleadoResponse(ClienteResponse datosPersonales, BigDecimal salario, LocalDate fechaContratacion,
                               Long idCargo, String nombreCargo, Long idSede, String nombreSede) {

    public static EmpleadoResponse from(Empleado empleado) {
        return new EmpleadoResponse(
                ClienteResponse.from(empleado),
                empleado.getSalario(),
                empleado.getFechaContratacion(),
                empleado.getCargo().getIdCargo(),
                empleado.getCargo().getNombreCargo(),
                empleado.getSede().getIdSede(),
                empleado.getSede().getNombreSede());
    }
}
