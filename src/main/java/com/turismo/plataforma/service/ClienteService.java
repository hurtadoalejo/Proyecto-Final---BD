package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.cliente.ClienteActualizarRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.dto.cliente.ClienteResumenResponse;
import com.turismo.plataforma.dto.cliente.MillasResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.ReservaRepository;
import com.turismo.plataforma.repository.ReservaViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Los clientes se crean con POST /api/auth/registro; aquí se consultan, modifican y eliminan. */
@Service
@Transactional(readOnly = true)
public class ClienteService {

    /** 1 kilómetro = 0,621371 millas. */
    private static final BigDecimal MILLAS_POR_KM = new BigDecimal("0.621371");

    private final ClienteRepository clienteRepository;
    private final ReservaViajeRepository reservaViajeRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;

    public ClienteService(ClienteRepository clienteRepository, ReservaViajeRepository reservaViajeRepository,
                          ReservaRepository reservaRepository, UsuarioService usuarioService) {
        this.clienteRepository = clienteRepository;
        this.reservaViajeRepository = reservaViajeRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioService = usuarioService;
    }

    /** Clientes por identificación o primer apellido (todos si q viene vacío). */
    public List<ClienteResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return clienteRepository.buscar(filtro).stream()
                .map(ClienteResponse::from)
                .toList();
    }

    /**
     * Clientes con el dinero que han gastado (pagos APROBADOS), del que más gastó al que menos.
     * gastoMin y gastoMax son opcionales e incluyen el límite.
     */
    public List<ClienteResumenResponse> listarConGasto(String texto, BigDecimal gastoMin, BigDecimal gastoMax) {
        if (gastoMin != null && gastoMax != null && gastoMin.compareTo(gastoMax) > 0) {
            throw new SolicitudInvalidaException("El gasto mínimo no puede ser mayor que el gasto máximo");
        }

        Map<Long, BigDecimal> gastado = new HashMap<>();
        Map<Long, Long> compras = new HashMap<>();
        for (Object[] fila : clienteRepository.gastoPorCliente()) {
            Long idCliente = ((Number) fila[0]).longValue();
            compras.put(idCliente, ((Number) fila[1]).longValue());
            gastado.put(idCliente, new BigDecimal(fila[2].toString()));
        }
        Map<Long, Long> reservas = new HashMap<>();
        for (Object[] fila : reservaRepository.contarPorCliente()) {
            reservas.put(((Number) fila[0]).longValue(), ((Number) fila[1]).longValue());
        }

        String filtro = texto == null ? "" : texto.trim();
        return clienteRepository.buscar(filtro).stream()
                .map(cliente -> {
                    Long id = cliente.getIdUsuario();
                    return new ClienteResumenResponse(ClienteResponse.from(cliente),
                            gastado.getOrDefault(id, BigDecimal.ZERO),
                            compras.getOrDefault(id, 0L),
                            reservas.getOrDefault(id, 0L));
                })
                .filter(c -> gastoMin == null || c.totalGastado().compareTo(gastoMin) >= 0)
                .filter(c -> gastoMax == null || c.totalGastado().compareTo(gastoMax) <= 0)
                .sorted(Comparator.comparing(ClienteResumenResponse::totalGastado).reversed())
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

    /** Falla con 409 si el cliente tiene reservas (sus compras y facturas deben conservarse). */
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = buscarEntidad(id);
        long reservas = reservaRepository.countByClienteIdUsuario(id);
        if (reservas > 0) {
            throw new ConflictoException("No se puede eliminar al cliente porque tiene " + reservas
                    + (reservas == 1 ? " reserva registrada" : " reservas registradas")
                    + ". Sus compras y facturas deben conservarse.");
        }
        clienteRepository.delete(cliente);
        clienteRepository.flush();
    }

    private Cliente buscarEntidad(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente con id " + id));
    }
}
