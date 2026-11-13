package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.empleado.EmpleadoActualizarRequest;
import com.turismo.plataforma.dto.empleado.EmpleadoRequest;
import com.turismo.plataforma.dto.empleado.EmpleadoResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.Empleado;
import com.turismo.plataforma.model.Sede;
import com.turismo.plataforma.model.catalogo.Cargo;
import com.turismo.plataforma.model.catalogo.Rol;
import com.turismo.plataforma.repository.EmpleadoRepository;
import com.turismo.plataforma.repository.SedeRepository;
import com.turismo.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Empleados: rol EMPLEADO o ADMIN, con cargo y sede. */
@Service
@Transactional(readOnly = true)
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final SedeRepository sedeRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final CatalogoService catalogoService;

    public EmpleadoService(EmpleadoRepository empleadoRepository, SedeRepository sedeRepository,
                           UsuarioRepository usuarioRepository, UsuarioService usuarioService,
                           CatalogoService catalogoService) {
        this.empleadoRepository = empleadoRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.catalogoService = catalogoService;
    }

    /** Todos los empleados, o los de una sede. */
    public List<EmpleadoResponse> listar(Long idSede) {
        return empleadoRepository.listar(idSede).stream()
                .map(EmpleadoResponse::from)
                .toList();
    }

    public EmpleadoResponse obtener(Long id) {
        return EmpleadoResponse.from(buscarEntidad(id));
    }

    @Transactional
    public EmpleadoResponse crear(EmpleadoRequest request) {
        usuarioService.validarDatosUnicos(request, null);
        Rol rol = rolDeEmpleado(request.rol());
        Cargo cargo = catalogoService.cargo(request.idCargo());
        Sede sede = buscarSede(request.idSede());

        Empleado empleado = new Empleado();
        usuarioService.aplicarDatosPersonales(empleado, request);
        usuarioService.asignarContrasenia(empleado, request.contrasenia());
        empleado.setRol(rol);
        empleado.setCargo(cargo);
        empleado.setSede(sede);
        empleado.setSalario(request.salario());
        empleado.setFechaContratacion(request.fechaContratacion());
        return EmpleadoResponse.from(empleadoRepository.save(empleado));
    }

    /** Modifica datos personales y laborales (la contraseña no cambia aquí). */
    @Transactional
    public EmpleadoResponse actualizar(Long id, EmpleadoActualizarRequest request) {
        Empleado empleado = buscarEntidad(id);
        usuarioService.validarDatosUnicos(request, id);
        Rol rol = rolDeEmpleado(request.rol());
        Cargo cargo = catalogoService.cargo(request.idCargo());
        Sede sede = buscarSede(request.idSede());
        if (esAdmin(empleado) && !Rol.ADMIN.equals(rol.getNombreRol())) {
            validarQueQuedeOtroAdmin();
        }

        usuarioService.aplicarDatosPersonales(empleado, request);
        empleado.setRol(rol);
        empleado.setCargo(cargo);
        empleado.setSede(sede);
        empleado.setSalario(request.salario());
        empleado.setFechaContratacion(request.fechaContratacion());
        return EmpleadoResponse.from(empleado);
    }

    /** Falla con 409 si atendió reservas o si es el único administrador. */
    @Transactional
    public void eliminar(Long id) {
        Empleado empleado = buscarEntidad(id);
        if (esAdmin(empleado)) {
            validarQueQuedeOtroAdmin();
        }
        empleadoRepository.delete(empleado);
        empleadoRepository.flush();
    }

    private Rol rolDeEmpleado(String nombreRol) {
        Rol rol = catalogoService.rol(nombreRol);
        if (Rol.CLIENTE.equals(rol.getNombreRol())) {
            throw new SolicitudInvalidaException("Un empleado solo puede tener rol EMPLEADO o ADMIN");
        }
        return rol;
    }

    private static boolean esAdmin(Empleado empleado) {
        return Rol.ADMIN.equals(empleado.getRol().getNombreRol());
    }

    private void validarQueQuedeOtroAdmin() {
        if (usuarioRepository.countByRolNombreRol(Rol.ADMIN) <= 1) {
            throw new ConflictoException("Debe quedar al menos un administrador");
        }
    }

    private Sede buscarSede(Long idSede) {
        return sedeRepository.findById(idSede)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la sede con id " + idSede));
    }

    private Empleado buscarEntidad(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el empleado con id " + id));
    }
}
