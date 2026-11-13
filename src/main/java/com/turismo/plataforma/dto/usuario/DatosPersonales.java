package com.turismo.plataforma.dto.usuario;

import java.time.LocalDate;

/**
 * Datos personales que el frontend recolecta en el registro (mismos nombres de campo).
 * pais, departamento y ciudad son NOMBRES (los del formulario de registro del frontend).
 */
public interface DatosPersonales {

    String tipoIdentificacion();

    String numeroIdentificacion();

    String primerNombre();

    String segundoNombre();

    String primerApellido();

    String segundoApellido();

    LocalDate fechaNacimiento();

    String pais();

    String departamento();

    String ciudad();

    String direccion();

    String indicativo();

    String telefono();

    String correo();
}
