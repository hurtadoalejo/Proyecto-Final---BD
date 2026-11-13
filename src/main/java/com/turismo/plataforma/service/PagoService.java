package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.pago.PagoRequest;
import com.turismo.plataforma.dto.pago.PagoResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Factura;
import com.turismo.plataforma.model.Reserva;
import com.turismo.plataforma.model.Transaccion;
import com.turismo.plataforma.model.catalogo.EstadoReserva;
import com.turismo.plataforma.model.catalogo.EstadoTransaccion;
import com.turismo.plataforma.model.catalogo.MetodoPago;
import com.turismo.plataforma.repository.ReservaRepository;
import com.turismo.plataforma.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Pago de una reserva PENDIENTE: crea la Transacción (APROBADA) por el valor total,
 * emite la Factura y deja la reserva CONFIRMADA.
 */
@Service
@Transactional(readOnly = true)
public class PagoService {

    private final ReservaRepository reservaRepository;
    private final TransaccionRepository transaccionRepository;
    private final CatalogoService catalogoService;

    public PagoService(ReservaRepository reservaRepository, TransaccionRepository transaccionRepository,
                       CatalogoService catalogoService) {
        this.reservaRepository = reservaRepository;
        this.transaccionRepository = transaccionRepository;
        this.catalogoService = catalogoService;
    }

    public PagoResponse obtener(Long idReserva) {
        Transaccion transaccion = transaccionRepository.findById(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("La reserva " + idReserva + " no tiene pago"));
        return PagoResponse.from(transaccion);
    }

    @Transactional
    public PagoResponse pagar(Long idReserva, PagoRequest request) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la reserva con id " + idReserva));
        if (reserva.getTransaccion() != null) {
            throw new ConflictoException("La reserva " + idReserva + " ya fue pagada");
        }
        if (!reserva.tieneEstado(EstadoReserva.PENDIENTE)) {
            throw new ConflictoException("Solo se pagan reservas PENDIENTES; esta está "
                    + reserva.getEstadoReserva().getNombreEstadoReserva());
        }
        MetodoPago metodoPago = catalogoService.metodoPago(request.metodoPago());
        EstadoTransaccion aprobada = catalogoService.estadoTransaccion(EstadoTransaccion.APROBADA);
        EstadoReserva confirmada = catalogoService.estadoReserva(EstadoReserva.CONFIRMADA);
        LocalDateTime ahora = LocalDateTime.now();

        Transaccion transaccion = new Transaccion();
        transaccion.setReserva(reserva);
        transaccion.setMontoPagado(reserva.calcularValorTotal());
        transaccion.setFechaPago(ahora);
        transaccion.setMetodoPago(metodoPago);
        transaccion.setEstadoTransaccion(aprobada);

        Factura factura = new Factura();
        factura.setTransaccion(transaccion);
        factura.setFechaEmision(ahora);
        transaccion.setFactura(factura);

        reserva.setTransaccion(transaccion);
        reserva.setEstadoReserva(confirmada);
        return PagoResponse.from(transaccionRepository.save(transaccion));
    }
}
