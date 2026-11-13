package com.turismo.plataforma.dto.empleado;

import com.turismo.plataforma.dto.usuario.DatosPersonales;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Modificación de un empleado (sin contraseña). */
public record EmpleadoActualizarRequest(
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

        @NotNull(message = "El salario es obligatorio")
        @PositiveOrZero(message = "El salario no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El salario admite máximo 2 decimales")
        BigDecimal salario,

        @NotNull(message = "La fecha de contratación es obligatoria")
        @PastOrPresent(message = "La fecha de contratación no puede ser futura")
        LocalDate fechaContratacion,

        @NotNull(message = "El cargo es obligatorio (id de /api/catalogos/cargos)")
        Long idCargo,

        @NotNull(message = "La sede es obligatoria")
        Long idSede,

        @NotBlank(message = "El rol es obligatorio (EMPLEADO o ADMIN)")
        String rol) implements DatosPersonales {
}
