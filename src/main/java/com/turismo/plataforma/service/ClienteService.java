package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.cliente.ClienteActualizarRequest;
import com.turismo.plataforma.dto.cliente.ClienteRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.dto.cliente.MillasResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.enums.EstadoReserva;
import com.turismo.plataforma.model.enums.Rol;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.ReservaTrayectoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    /** 1 kilómetro = 0,621371 millas. */
    private static final BigDecimal MILLAS_POR_KM = new BigDecimal("0.621371");

    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;
    private final ReservaTrayectoRepository reservaTrayectoRepository;

    public ClienteService(ClienteRepository clienteRepository, UsuarioService usuarioService,
                          ReservaTrayectoRepository reservaTrayectoRepository) {
        this.clienteRepository = clienteRepository;
        this.usuarioService = usuarioService;
        this.reservaTrayectoRepository = reservaTrayectoRepository;
    }

    /** Consulta: clientes por identificación o primer apellido (todos si viene vacío). */
    public List<ClienteResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return clienteRepository.buscar(filtro).stream()
                .map(ClienteResponse::from)
                .toList();
    }

    /** Consulta: un cliente por su id. */
    public ClienteResponse obtener(Long id) {
        return ClienteResponse.from(buscarEntidad(id));
    }

    /** Atributo derivado: millas por los kilómetros de sus reservas confirmadas. */
    public MillasResponse calcularMillas(Long id) {
        Cliente cliente = buscarEntidad(id);
        BigDecimal kilometros = reservaTrayectoRepository.sumarDistanciaRecorrida(
                cliente.getIdUsuario(), EstadoReserva.CONFIRMADA);
        if (kilometros == null) {
            kilometros = BigDecimal.ZERO;
        }
        BigDecimal millas = kilometros.multiply(MILLAS_POR_KM).setScale(0, RoundingMode.DOWN);
        return new MillasResponse(cliente.getIdUsuario(), kilometros, millas);
    }

    /** Registro: correo e identificación únicos, contraseña cifrada y rol CLIENTE. */
    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        usuarioService.validarDatosUnicos(request, null);
        Cliente cliente = new Cliente();
        usuarioService.aplicarDatosPersonales(cliente, request);
        usuarioService.asignarContrasenia(cliente, request.contrasenia());
        cliente.setRol(Rol.CLIENTE);
        return ClienteResponse.from(clienteRepository.save(cliente));
    }

    /** Modificación de los datos personales (la contraseña no cambia aquí). */
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteActualizarRequest request) {
        Cliente cliente = buscarEntidad(id);
        usuarioService.validarDatosUnicos(request, id);
        usuarioService.aplicarDatosPersonales(cliente, request);
        return ClienteResponse.from(cliente);
    }

    /** Eliminación: falla con 409 si el cliente tiene reservas o notificaciones. */
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = buscarEntidad(id);
        clienteRepository.delete(cliente);
        clienteRepository.flush();
    }

    private Cliente buscarEntidad(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente con id " + id));
    }
}