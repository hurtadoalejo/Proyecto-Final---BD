package com.turismo.plataforma.dto.usuario;

import java.time.LocalDate;

public interface DatosPersonales {

    String primerNombre();

    String segundoNombre();

    String primerApellido();

    String segundoApellido();

    String identificacion();

    LocalDate fechaNacimiento();

    String direccion();

    String telefono();

    String correo();
}