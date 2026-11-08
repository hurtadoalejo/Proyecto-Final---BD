package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.empleado.EmpleadoActualizarRequest;
import com.turismo.plataforma.dto.empleado.EmpleadoRequest;
import com.turismo.plataforma.dto.empleado.EmpleadoResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Empleado;
import com.turismo.plataforma.model.Sede;
import com.turismo.plataforma.model.enums.Rol;
import com.turismo.plataforma.repository.EmpleadoRepository;
import com.turismo.plataforma.repository.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final SedeRepository sedeRepository;
    private final UsuarioService usuarioService;

    public EmpleadoService(EmpleadoRepository empleadoRepository, SedeRepository sedeRepository,
                           UsuarioService usuarioService) {
        this.empleadoRepository = empleadoRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioService = usuarioService;
    }

    public List<EmpleadoResponse> listar(Long idSede) {
        List<Empleado> empleados;
        if (idSede == null) {
            empleados = empleadoRepository.listarConSede();
        } else {
            buscarSede(idSede);
            empleados = empleadoRepository.listarPorSede(idSede);
        }
        return empleados.stream()
                .map(EmpleadoResponse::from)
                .toList();
    }

    public EmpleadoResponse obtener(Long id) {
        return EmpleadoResponse.from(buscarEntidad(id));
    }

    @Transactional
    public EmpleadoResponse crear(EmpleadoRequest request) {
        validarRol(request.rol());
        usuarioService.validarDatosUnicos(request, null);
        Sede sede = buscarSede(request.idSede());

        Empleado empleado = new Empleado();
        usuarioService.aplicarDatosPersonales(empleado, request);
        usuarioService.asignarContrasenia(empleado, request.contrasenia());
        aplicarDatosLaborales(empleado, request.cargo(), request.salario(),
                request.fechaContratacion(), sede, request.rol());
        return EmpleadoResponse.from(empleadoRepository.save(empleado));
    }

    @Transactional
    public EmpleadoResponse actualizar(Long id, EmpleadoActualizarRequest request) {
        Empleado empleado = buscarEntidad(id);
        validarRol(request.rol());
        usuarioService.validarDatosUnicos(request, id);
        Sede sede = buscarSede(request.idSede());

        usuarioService.aplicarDatosPersonales(empleado, request);
        aplicarDatosLaborales(empleado, request.cargo(), request.salario(),
                request.fechaContratacion(), sede, request.rol());
        return EmpleadoResponse.from(empleado);
    }

    @Transactional
    public void eliminar(Long id) {
        Empleado empleado = buscarEntidad(id);
        empleadoRepository.delete(empleado);
        empleadoRepository.flush();
    }

    private Empleado buscarEntidad(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el empleado con id " + id));
    }

    private Sede buscarSede(Long idSede) {
        return sedeRepository.findById(idSede)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la sede con id " + idSede));
    }

    private void validarRol(Rol rol) {
        if (rol == Rol.CLIENTE) {
            throw new ConflictoException("Un empleado solo puede tener rol EMPLEADO o ADMIN");
        }
    }

    private void aplicarDatosLaborales(Empleado empleado, String cargo, BigDecimal salario,
                                       LocalDate fechaContratacion, Sede sede, Rol rol) {
        empleado.setCargo(cargo.trim());
        empleado.setSalario(salario);
        empleado.setFechaContratacion(fechaContratacion);
        empleado.setSede(sede);
        empleado.setRol(rol);
    }
}