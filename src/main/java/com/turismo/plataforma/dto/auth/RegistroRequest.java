package com.turismo.plataforma.dto.auth;

import com.turismo.plataforma.dto.usuario.DatosPersonales;
import jakarta.validation.constraints.*;

/** Registro de un cliente. Mismos campos que envía el frontend. */
public record RegistroRequest(
        @NotBlank(message = "El tipo de identificación es obligatorio (CC, CE, TI, PA, PEP)")
        String tipoIdentificacion,

        @NotBlank(message = "El número de identificación es obligatorio")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String numeroIdentificacion,

        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String primerNombre,

        @Size(max = 20, message = "Máximo 20 caracteres")
        String segundoNombre,

        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String primerApellido,

        @Size(max = 20, message = "Máximo 20 caracteres")
        String segundoApellido,

        @NotNull(message = "El país es obligatorio (id de /api/catalogos/paises)")
        Long pais,

        @NotNull(message = "El departamento es obligatorio (id de /api/catalogos/paises/{idPais}/departamentos)")
        Long departamento,

        @NotNull(message = "La ciudad es obligatoria (id de /api/catalogos/departamentos/{idDepartamento}/ciudades)")
        Long ciudad,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 50, message = "Máximo 50 caracteres")
        String direccion,

        @NotBlank(message = "El indicativo del país es obligatorio")
        @Pattern(regexp = "^\\+\\d{1,4}$", message = "El indicativo debe tener el formato +57")
        String indicativoPais,

        @NotBlank(message = "El celular es obligatorio")
        @Pattern(regexp = "^\\d{7,15}$", message = "El celular debe tener entre 7 y 15 dígitos, sin espacios")
        String telefonoCelular,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no es válido")
        @Size(max = 100, message = "Máximo 100 caracteres")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        String contrasenia) implements DatosPersonales {
}
