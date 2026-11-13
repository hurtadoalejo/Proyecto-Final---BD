package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.notificacion.NotificacionRequest;
import com.turismo.plataforma.dto.notificacion.NotificacionResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Notificacion;
import com.turismo.plataforma.model.Oferta;
import com.turismo.plataforma.model.catalogo.EstadoNotificacion;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.NotificacionRepository;
import com.turismo.plataforma.repository.OfertaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Notificaciones a uno o varios clientes (N:M por la tabla ClienteNotificacion). */
@Service
@Transactional(readOnly = true)
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final ClienteRepository clienteRepository;
    private final OfertaRepository ofertaRepository;
    private final CatalogoService catalogoService;

    public NotificacionService(NotificacionRepository notificacionRepository, ClienteRepository clienteRepository,
                               OfertaRepository ofertaRepository, CatalogoService catalogoService) {
        this.notificacionRepository = notificacionRepository;
        this.clienteRepository = clienteRepository;
        this.ofertaRepository = ofertaRepository;
        this.catalogoService = catalogoService;
    }

    /** Todas, o solo las de un cliente. */
    public List<NotificacionResponse> listar(Long idCliente) {
        List<Notificacion> notificaciones = idCliente == null
                ? notificacionRepository.listar()
                : notificacionRepository.listarPorCliente(idCliente);
        return notificaciones.stream().map(NotificacionResponse::from).toList();
    }

    public NotificacionResponse obtener(Long id) {
        return NotificacionResponse.from(buscarEntidad(id));
    }

    @Transactional
    public NotificacionResponse crear(NotificacionRequest request) {
        Datos datos = validar(request);
        Notificacion notificacion = new Notificacion();
        aplicar(notificacion, request, datos);
        return NotificacionResponse.from(notificacionRepository.save(notificacion));
    }

    @Transactional
    public NotificacionResponse actualizar(Long id, NotificacionRequest request) {
        Notificacion notificacion = buscarEntidad(id);
        Datos datos = validar(request);
        aplicar(notificacion, request, datos);
        return NotificacionResponse.from(notificacion);
    }

    @Transactional
    public void eliminar(Long id) {
        notificacionRepository.delete(buscarEntidad(id));
        notificacionRepository.flush();
    }

    private record Datos(EstadoNotificacion estado, Oferta oferta, List<Cliente> clientes) {
    }

    private Datos validar(NotificacionRequest request) {
        EstadoNotificacion estado = catalogoService.estadoNotificacion(request.estadoNotificacion());
        Oferta oferta = request.idOferta() == null ? null : ofertaRepository.findById(request.idOferta())
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la oferta con id " + request.idOferta()));

        List<Cliente> clientes = clienteRepository.findAllById(request.idsClientes());
        if (clientes.size() != request.idsClientes().size()) {
            Set<Long> encontrados = clientes.stream().map(Cliente::getIdUsuario).collect(Collectors.toSet());
            Set<Long> faltantes = new HashSet<>(request.idsClientes());
            faltantes.removeAll(encontrados);
            throw new SolicitudInvalidaException("No existen los clientes con id " + faltantes);
        }
        return new Datos(estado, oferta, clientes);
    }

    /** fechaEnvio se llena cuando el estado es ENVIADO. */
    private static void aplicar(Notificacion notificacion, NotificacionRequest request, Datos datos) {
        boolean enviada = EstadoNotificacion.ENVIADO.equals(datos.estado().getNombreEstadoNotificacion());
        notificacion.setMensaje(request.mensaje().trim());
        notificacion.setEstadoNotificacion(datos.estado());
        notificacion.setOferta(datos.oferta());
        if (!enviada) {
            notificacion.setFechaEnvio(null);
        } else if (notificacion.getFechaEnvio() == null) {
            notificacion.setFechaEnvio(LocalDateTime.now());
        }
        notificacion.getClientes().clear();
        notificacion.getClientes().addAll(datos.clientes());
    }

    private Notificacion buscarEntidad(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la notificación con id " + id));
    }
}
