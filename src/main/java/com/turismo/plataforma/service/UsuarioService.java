package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.usuario.DatosPersonales;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.NombreCompleto;
import com.turismo.plataforma.model.Usuario;
import com.turismo.plataforma.model.catalogo.TipoIdentificacion;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.model.ubicacion.Departamento;
import com.turismo.plataforma.repository.CiudadRepository;
import com.turismo.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

/** Lógica común de Cliente y Empleado (superclase Usuario). Se usa dentro de las transacciones de ellos. */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CiudadRepository ciudadRepository;
    private final CatalogoService catalogoService;

    public UsuarioService(UsuarioRepository usuarioRepository, CiudadRepository ciudadRepository,
                          CatalogoService catalogoService) {
        this.usuarioRepository = usuarioRepository;
        this.ciudadRepository = ciudadRepository;
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
        Ciudad ciudad = validarUbicacion(datos);

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
        usuario.setIndicativoPais(datos.indicativoPais().trim());
        usuario.setTelefono(datos.telefonoCelular().trim());
        usuario.setCorreo(normalizarCorreo(datos.correo()));
    }

    /** Guarda la contraseña tal como llega (sin cifrar). */
    public void asignarContrasenia(Usuario usuario, String contrasenia) {
        usuario.setContrasenia(contrasenia);
    }

    public static String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase(Locale.ROOT);
    }

    /** La ciudad debe existir y pertenecer al departamento y país enviados. */
    private Ciudad validarUbicacion(DatosPersonales datos) {
        Ciudad ciudad = ciudadRepository.buscarConUbicacion(datos.ciudad())
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la ciudad con id " + datos.ciudad()));
        Departamento departamento = ciudad.getDepartamento();
        if (!departamento.getIdDepartamento().equals(datos.departamento())) {
            throw new SolicitudInvalidaException("La ciudad " + ciudad.getNombreCiudad()
                    + " no pertenece al departamento con id " + datos.departamento());
        }
        if (!departamento.getPais().getIdPais().equals(datos.pais())) {
            throw new SolicitudInvalidaException("El departamento " + departamento.getNombreDepartamento()
                    + " no pertenece al país con id " + datos.pais());
        }
        return ciudad;
    }

    private static String opcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}
