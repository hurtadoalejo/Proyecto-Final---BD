package com.turismo.plataforma.dto.empleado;

import com.turismo.plataforma.model.Empleado;
import com.turismo.plataforma.model.NombreCompleto;
import com.turismo.plataforma.model.Sede;
import com.turismo.plataforma.model.enums.Rol;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoResponse(Long idUsuario, String primerNombre, String segundoNombre,
                               String primerApellido, String segundoApellido, String identificacion,
                               LocalDate fechaNacimiento, String direccion, String telefono, String correo,
                               Rol rol, String cargo, BigDecimal salario, LocalDate fechaContratacion,
                               Long idSede, String nombreSede) {

    public static EmpleadoResponse from(Empleado empleado) {
        NombreCompleto nombre = empleado.getNombre();
        Sede sede = empleado.getSede();
        return new EmpleadoResponse(empleado.getIdUsuario(), nombre.getPrimerNombre(), nombre.getSegundoNombre(),
                nombre.getPrimerApellido(), nombre.getSegundoApellido(), empleado.getIdentificacion(),
                empleado.getFechaNacimiento(), empleado.getDireccion(), empleado.getTelefono(),
                empleado.getCorreo(), empleado.getRol(), empleado.getCargo(), empleado.getSalario(),
                empleado.getFechaContratacion(), sede.getIdSede(), sede.getNombreSede());
    }
}