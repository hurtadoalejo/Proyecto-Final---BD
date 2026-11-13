package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.auth.LoginRequest;
import com.turismo.plataforma.dto.auth.LoginResponse;
import com.turismo.plataforma.dto.auth.RegistroRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.exception.CredencialesInvalidasException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Empleado;
import com.turismo.plataforma.model.Usuario;
import com.turismo.plataforma.model.catalogo.Rol;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Registro de clientes y login (solo valida correo y contraseña). */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;
    private final CatalogoService catalogoService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository,
                       UsuarioService usuarioService, CatalogoService catalogoService,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioService = usuarioService;
        this.catalogoService = catalogoService;
        this.passwordEncoder = passwordEncoder;
    }

    /** Crea un Cliente con rol CLIENTE. Correo e identificación no se pueden repetir. */
    @Transactional
    public ClienteResponse registrar(RegistroRequest request) {
        usuarioService.validarDatosUnicos(request, null);
        Rol rolCliente = catalogoService.rol(Rol.CLIENTE);

        Cliente cliente = new Cliente();
        usuarioService.aplicarDatosPersonales(cliente, request);
        usuarioService.asignarContrasenia(cliente, request.contrasenia());
        cliente.setRol(rolCliente);
        return ClienteResponse.from(clienteRepository.save(cliente));
    }

    /** Correo y contraseña correctos -> datos del usuario. Si no, 401 (mismo mensaje en ambos casos). */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.buscarPorCorreo(UsuarioService.normalizarCorreo(request.correo()))
                .filter(u -> passwordEncoder.matches(request.contrasenia(), u.getContrasenia()))
                .orElseThrow(CredencialesInvalidasException::new);

        String tipoUsuario = usuario instanceof Empleado ? "EMPLEADO" : "CLIENTE";
        return new LoginResponse(usuario.getIdUsuario(), usuario.getNombre().completo(), usuario.getCorreo(),
                usuario.getRol().getNombreRol(), tipoUsuario);
    }
}
