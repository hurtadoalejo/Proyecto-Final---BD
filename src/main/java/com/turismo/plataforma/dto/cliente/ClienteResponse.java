package com.turismo.plataforma.dto.cliente;

import com.turismo.plataforma.model.Usuario;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.model.ubicacion.Departamento;

import java.time.LocalDate;

/** Datos de un cliente (mismos nombres de campo del registro). Nunca incluye la contraseña. */
public record ClienteResponse(Long idUsuario, String tipoIdentificacion, String numeroIdentificacion,
                              String primerNombre, String segundoNombre, String primerApellido,
                              String segundoApellido, LocalDate fechaNacimiento, Long pais, String nombrePais, Long departamento,
                              String nombreDepartamento, Long ciudad, String nombreCiudad, String direccion,
                              String indicativoPais, String telefonoCelular, String correo, String rol) {

    public static ClienteResponse from(Usuario usuario) {
        Ciudad ciudad = usuario.getCiudad();
        Departamento departamento = ciudad.getDepartamento();
        return new ClienteResponse(
                usuario.getIdUsuario(),
                usuario.getTipoIdentificacion().getNombreIdentificacion(),
                usuario.getIdentificacion(),
                usuario.getNombre().getPrimerNombre(),
                usuario.getNombre().getSegundoNombre(),
                usuario.getNombre().getPrimerApellido(),
                usuario.getNombre().getSegundoApellido(),
                usuario.getFechaNacimiento(),
                departamento.getPais().getIdPais(),
                departamento.getPais().getNombrePais(),
                departamento.getIdDepartamento(),
                departamento.getNombreDepartamento(),
                ciudad.getIdCiudad(),
                ciudad.getNombreCiudad(),
                usuario.getDireccion(),
                usuario.getIndicativoPais(),
                usuario.getTelefono(),
                usuario.getCorreo(),
                usuario.getRol().getNombreRol());
    }
}
