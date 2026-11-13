package com.turismo.plataforma.dto.auth;

import com.turismo.plataforma.dto.usuario.DatosPersonales;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

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

        @NotNull(message = "La fecha de nacimiento es obligatoria (formato 2000-05-31)")
        @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
        LocalDate fechaNacimiento,

        @NotBlank(message = "El país es obligatorio")
        @Size(max = 25, message = "Máximo 25 caracteres")
        String pais,

        @NotBlank(message = "El departamento es obligatorio")
        @Size(max = 40, message = "Máximo 40 caracteres")
        String departamento,

        @NotBlank(message = "La ciudad es obligatoria")
        @Size(max = 40, message = "Máximo 40 caracteres")
        String ciudad,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 50, message = "Máximo 50 caracteres")
        String direccion,

        @NotBlank(message = "El indicativo es obligatorio")
        @Pattern(regexp = "^\\+\\d{1,4}$", message = "El indicativo debe tener el formato +57")
        String indicativo,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9 -]{7,20}$", message = "El teléfono solo admite números, espacios y guiones")
        String telefono,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no es válido")
        @Size(max = 100, message = "Máximo 100 caracteres")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
        String contrasena) implements DatosPersonales {
}
