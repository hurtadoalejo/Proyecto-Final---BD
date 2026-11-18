package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.usuario.DatosPersonales;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.NombreCompleto;
import com.turismo.plataforma.model.Usuario;
import com.turismo.plataforma.model.catalogo.TipoIdentificacion;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

/** Lógica común de Cliente y Empleado (superclase Usuario). Se usa dentro de las transacciones de ellos. */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CatalogoService catalogoService;

    public UsuarioService(UsuarioRepository usuarioRepository, CatalogoService catalogoService) {
        this.usuarioRepository = usuarioRepository;
        this.catalogoService = catalogoService;
    }

    /** Correo e identificación únicos. idUsuarioActual = null al crear; el id propio al modificar. */
    public void validarDatosUnicos(DatosPersonales datos, Long idUsuarioActual) {
        String correo = normalizarCorreo(datos.correo());
        String identificacion = datos.numeroIdentificacion().trim();

        boolean correoEnUso = idUsuarioActual == null
                ? usuarioRepository.existsByCorreoIgnoreCase(correo)
                : usuarioRepository.existsByCorreoIgnoreCaseAndIdUsuarioNot(correo, idUsuarioActual);
        if (correoEnUso) {
            throw new ConflictoException("Ya existe un usuario con el correo " + correo);
        }

        boolean identificacionEnUso = idUsuarioActual == null
                ? usuarioRepository.existsByIdentificacion(identificacion)
                : usuarioRepository.existsByIdentificacionAndIdUsuarioNot(identificacion, idUsuarioActual);
        if (identificacionEnUso) {
            throw new ConflictoException("Ya existe un usuario con la identificación " + identificacion);
        }
    }

    /**
     * Copia los datos del formulario al usuario. Primero consulta todo lo que necesita
     * (tipo de identificación y ciudad) y después modifica la entidad.
     */
    public void aplicarDatosPersonales(Usuario usuario, DatosPersonales datos) {
        TipoIdentificacion tipoIdentificacion = catalogoService.tipoIdentificacion(datos.tipoIdentificacion());
        Ciudad ciudad = catalogoService.ciudad(datos.pais(), datos.departamento(), datos.ciudad());
        String telefono = unirTelefono(datos.indicativo(), datos.telefono());

        NombreCompleto nombre = usuario.getNombre();
        nombre.setPrimerNombre(datos.primerNombre().trim());
        nombre.setSegundoNombre(opcional(datos.segundoNombre()));
        nombre.setPrimerApellido(datos.primerApellido().trim());
        nombre.setSegundoApellido(opcional(datos.segundoApellido()));

        usuario.setTipoIdentificacion(tipoIdentificacion);
        usuario.setIdentificacion(datos.numeroIdentificacion().trim());
        usuario.setFechaNacimiento(datos.fechaNacimiento());
        usuario.setCiudad(ciudad);
        usuario.setDireccion(datos.direccion().trim());
        usuario.setTelefono(telefono);
        usuario.setCorreo(normalizarCorreo(datos.correo()));
    }

    /** Guarda la contraseña tal como llega (sin cifrar). */
    public void asignarContrasenia(Usuario usuario, String contrasenia) {
        usuario.setContrasenia(contrasenia);
    }

    public static String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase(Locale.ROOT);
    }

    /** Guarda indicativo y número juntos en la columna telefono: "+57 3001234567". */
    private static String unirTelefono(String indicativo, String telefono) {
        String numero = telefono.replaceAll("\\D", "");
        if (numero.length() < 7 || numero.length() > 14) {
            throw new SolicitudInvalidaException("El teléfono debe tener entre 7 y 14 dígitos");
        }
        return indicativo.trim() + " " + numero;
    }

    private static String opcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}
