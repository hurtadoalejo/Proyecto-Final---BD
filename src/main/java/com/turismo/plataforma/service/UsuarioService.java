package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.usuario.DatosPersonales;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.model.NombreCompleto;
import com.turismo.plataforma.model.Usuario;
import com.turismo.plataforma.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validarDatosUnicos(DatosPersonales datos, Long idUsuario) {
        String correo = normalizarCorreo(datos.correo());
        String identificacion = datos.identificacion().trim();

        boolean correoEnUso = idUsuario == null
                ? usuarioRepository.existsByCorreoIgnoreCase(correo)
                : usuarioRepository.existsByCorreoIgnoreCaseAndIdUsuarioNot(correo, idUsuario);
        if (correoEnUso) {
            throw new ConflictoException("Ya existe un usuario con el correo " + correo);
        }

        boolean identificacionEnUso = idUsuario == null
                ? usuarioRepository.existsByIdentificacion(identificacion)
                : usuarioRepository.existsByIdentificacionAndIdUsuarioNot(identificacion, idUsuario);
        if (identificacionEnUso) {
            throw new ConflictoException("Ya existe un usuario con la identificación " + identificacion);
        }
    }

    public void aplicarDatosPersonales(Usuario usuario, DatosPersonales datos) {
        NombreCompleto nombre = usuario.getNombre();
        nombre.setPrimerNombre(datos.primerNombre().trim());
        nombre.setSegundoNombre(textoOpcional(datos.segundoNombre()));
        nombre.setPrimerApellido(datos.primerApellido().trim());
        nombre.setSegundoApellido(textoOpcional(datos.segundoApellido()));
        usuario.setIdentificacion(datos.identificacion().trim());
        usuario.setFechaNacimiento(datos.fechaNacimiento());
        usuario.setDireccion(textoOpcional(datos.direccion()));
        usuario.setTelefono(textoOpcional(datos.telefono()));
        usuario.setCorreo(normalizarCorreo(datos.correo()));
    }

    public void asignarContrasenia(Usuario usuario, String contrasenia) {
        usuario.setContrasenia(passwordEncoder.encode(contrasenia));
    }

    private String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase();
    }

    private String textoOpcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}