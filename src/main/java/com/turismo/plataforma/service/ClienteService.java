package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.cliente.ClienteActualizarRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.dto.cliente.MillasResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.ReservaViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** Los clientes se crean con POST /api/auth/registro; aquí se consultan, modifican y eliminan. */
@Service
@Transactional(readOnly = true)
public class ClienteService {

    /** 1 kilómetro = 0,621371 millas. */
    private static final BigDecimal MILLAS_POR_KM = new BigDecimal("0.621371");

    private final ClienteRepository clienteRepository;
    private final ReservaViajeRepository reservaViajeRepository;
    private final UsuarioService usuarioService;

    public ClienteService(ClienteRepository clienteRepository, ReservaViajeRepository reservaViajeRepository,
                          UsuarioService usuarioService) {
        this.clienteRepository = clienteRepository;
        this.reservaViajeRepository = reservaViajeRepository;
        this.usuarioService = usuarioService;
    }

    /** Clientes por identificación o primer apellido (todos si q viene vacío). */
    public List<ClienteResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return clienteRepository.buscar(filtro).stream()
                .map(ClienteResponse::from)
                .toList();
    }

    public ClienteResponse obtener(Long id) {
        return ClienteResponse.from(buscarEntidad(id));
    }

    /** Atributo derivado: millas por los kilómetros de sus reservas confirmadas. */
    public MillasResponse calcularMillas(Long id) {
        Cliente cliente = buscarEntidad(id);
        BigDecimal kilometros = reservaViajeRepository.sumarDistanciaRecorrida(cliente.getIdUsuario());
        if (kilometros == null) {
            kilometros = BigDecimal.ZERO;
        }
        BigDecimal millas = kilometros.multiply(MILLAS_POR_KM).setScale(0, RoundingMode.DOWN);
        return new MillasResponse(cliente.getIdUsuario(), kilometros, millas);
    }

    /** Modifica los datos personales (la contraseña no cambia aquí). */
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteActualizarRequest request) {
        Cliente cliente = buscarEntidad(id);
        usuarioService.validarDatosUnicos(request, id);
        usuarioService.aplicarDatosPersonales(cliente, request);
        return ClienteResponse.from(cliente);
    }

    /** Falla con 409 si el cliente tiene reservas. */
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
