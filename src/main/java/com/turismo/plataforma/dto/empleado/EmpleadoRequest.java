package com.turismo.plataforma.dto.empleado;

import com.turismo.plataforma.dto.usuario.DatosPersonales;
import com.turismo.plataforma.model.enums.Rol;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoRequest(
        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 50, message = "Máximo 50 caracteres")
        String primerNombre,

        @Size(max = 50, message = "Máximo 50 caracteres")
        String segundoNombre,

        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 50, message = "Máximo 50 caracteres")
        String primerApellido,

        @Size(max = 50, message = "Máximo 50 caracteres")
        String segundoApellido,

        @NotBlank(message = "La identificación es obligatoria")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String identificacion,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe estar en el pasado")
        LocalDate fechaNacimiento,

        @Size(max = 150, message = "Máximo 150 caracteres")
        String direccion,

        @Size(max = 20, message = "Máximo 20 caracteres")
        String telefono,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no es válido")
        @Size(max = 120, message = "Máximo 120 caracteres")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        String contrasenia,

        @NotBlank(message = "El cargo es obligatorio")
        @Size(max = 60, message = "Máximo 60 caracteres")
        String cargo,

        @NotNull(message = "El salario es obligatorio")
        @PositiveOrZero(message = "El salario no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El salario admite máximo 2 decimales")
        BigDecimal salario,

        @NotNull(message = "La fecha de contratación es obligatoria")
        @PastOrPresent(message = "La fecha de contratación no puede ser futura")
        LocalDate fechaContratacion,

        @NotNull(message = "La sede es obligatoria")
        Long idSede,

        @NotNull(message = "El rol es obligatorio (EMPLEADO o ADMIN)")
        Rol rol) implements DatosPersonales {
}