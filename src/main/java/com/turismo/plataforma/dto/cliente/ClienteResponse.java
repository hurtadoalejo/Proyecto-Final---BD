package com.turismo.plataforma.dto.cliente;

import com.turismo.plataforma.model.Usuario;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.model.ubicacion.Departamento;

import java.time.LocalDate;

/** Datos de un usuario con los mismos nombres de campo del formulario de registro. Nunca incluye la contraseña. */
public record ClienteResponse(Long idUsuario, String tipoIdentificacion, String numeroIdentificacion,
                              String primerNombre, String segundoNombre, String primerApellido,
                              String segundoApellido, LocalDate fechaNacimiento, String pais,
                              String departamento, String ciudad, String direccion, String indicativo,
                              String telefono, String correo, String rol) {

    public static ClienteResponse from(Usuario usuario) {
        Ciudad ciudad = usuario.getCiudad();
        Departamento departamento = ciudad.getDepartamento();
        String[] telefono = separarTelefono(usuario.getTelefono());
        return new ClienteResponse(
                usuario.getIdUsuario(),
                usuario.getTipoIdentificacion().getNombreIdentificacion(),
                usuario.getIdentificacion(),
                usuario.getNombre().getPrimerNombre(),
                usuario.getNombre().getSegundoNombre(),
                usuario.getNombre().getPrimerApellido(),
                usuario.getNombre().getSegundoApellido(),
                usuario.getFechaNacimiento(),
                departamento.getPais().getNombrePais(),
                departamento.getNombreDepartamento(),
                ciudad.getNombreCiudad(),
                usuario.getDireccion(),
                telefono[0],
                telefono[1],
                usuario.getCorreo(),
                usuario.getRol().getNombreRol());
    }

    /** "+57 3001234567" -> ["+57", "3001234567"]. */
    private static String[] separarTelefono(String telefono) {
        if (telefono == null) {
            return new String[]{null, null};
        }
        int espacio = telefono.indexOf(' ');
        if (telefono.startsWith("+") && espacio > 0) {
            return new String[]{telefono.substring(0, espacio), telefono.substring(espacio + 1)};
        }
        return new String[]{null, telefono};
    }
}
