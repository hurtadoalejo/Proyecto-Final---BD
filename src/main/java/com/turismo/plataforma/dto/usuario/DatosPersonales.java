package com.turismo.plataforma.dto.usuario;

/**
 * Datos personales que el frontend recolecta en el registro (mismos nombres de campo).
 * pais, departamento y ciudad son ids tomados de /api/catalogos.
 */
public interface DatosPersonales {

    String tipoIdentificacion();

    String numeroIdentificacion();

    String primerNombre();

    String segundoNombre();

    String primerApellido();

    String segundoApellido();

    Long pais();

    Long departamento();

    Long ciudad();

    String direccion();

    String indicativoPais();

    String telefonoCelular();

    String correo();
}
