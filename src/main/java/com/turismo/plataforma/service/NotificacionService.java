package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.notificacion.NotificacionRequest;
import com.turismo.plataforma.dto.notificacion.NotificacionResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Cupon;
import com.turismo.plataforma.model.Notificacion;
import com.turismo.plataforma.model.enums.EstadoNotificacion;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.CuponRepository;
import com.turismo.plataforma.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final ClienteRepository clienteRepository;
    private final CuponRepository cuponRepository;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               ClienteRepository clienteRepository,
                               CuponRepository cuponRepository) {
        this.notificacionRepository = notificacionRepository;
        this.clienteRepository = clienteRepository;
        this.cuponRepository = cuponRepository;
    }

    public List<NotificacionResponse> listar(Long idCliente) {
        List<Notificacion> notificaciones;
        if (idCliente == null) {
            notificaciones = notificacionRepository.listarConClienteYCupon();
        } else {
            buscarCliente(idCliente);
            notificaciones = notificacionRepository.listarPorCliente(idCliente);
        }
        return notificaciones.stream()
                .map(NotificacionResponse::from)
                .toList();
    }

    public NotificacionResponse obtener(Long id) {
        return NotificacionResponse.from(buscarEntidad(id));
    }

    @Transactional
    public NotificacionResponse crear(NotificacionRequest request) {
        Notificacion notificacion = new Notificacion();
        aplicarDatos(notificacion, request);
        return NotificacionResponse.from(notificacionRepository.save(notificacion));
    }

    @Transactional
    public NotificacionResponse actualizar(Long id, NotificacionRequest request) {
        Notificacion notificacion = buscarEntidad(id);
        aplicarDatos(notificacion, request);
        return NotificacionResponse.from(notificacion);
    }

    @Transactional
    public void eliminar(Long id) {
        Notificacion notificacion = buscarEntidad(id);
        notificacionRepository.delete(notificacion);
        notificacionRepository.flush();
    }

    private Notificacion buscarEntidad(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la notificación con id " + id));
    }

    private Cliente buscarCliente(Long idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente con id " + idCliente));
    }

    private Cupon buscarCuponVigente(Long idCupon) {
        if (idCupon == null) {
            return null;
        }
        Cupon cupon = cuponRepository.findById(idCupon)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cupón con id " + idCupon));
        if (cupon.getFechaFin().isBefore(LocalDate.now())) {
            throw new ConflictoException("El cupón " + cupon.getTitulo() + " está vencido");
        }
        return cupon;
    }

    private void aplicarDatos(Notificacion notificacion, NotificacionRequest request) {
        Cliente cliente = buscarCliente(request.idCliente());
        Cupon cupon = buscarCuponVigente(request.idCupon());
        notificacion.setCliente(cliente);
        notificacion.setCupon(cupon);
        notificacion.setMensaje(request.mensaje().trim());
        notificacion.setEstadoNotificacion(request.estadoNotificacion());
        if (request.estadoNotificacion() == EstadoNotificacion.ENVIADO) {
            if (notificacion.getFechaEnvio() == null) {
                notificacion.setFechaEnvio(LocalDateTime.now());
            }
        } else {
            notificacion.setFechaEnvio(null);
        }
    }
}