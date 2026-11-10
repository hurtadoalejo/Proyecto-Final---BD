package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.pago.PagoRequest;
import com.turismo.plataforma.dto.pago.PagoResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Factura;
import com.turismo.plataforma.model.Reserva;
import com.turismo.plataforma.model.Transaccion;
import com.turismo.plataforma.model.enums.EstadoReserva;
import com.turismo.plataforma.model.enums.EstadoTransaccion;
import com.turismo.plataforma.repository.ReservaRepository;
import com.turismo.plataforma.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class PagoService {

    private final ReservaRepository reservaRepository;
    private final TransaccionRepository transaccionRepository;

    public PagoService(ReservaRepository reservaRepository, TransaccionRepository transaccionRepository) {
        this.reservaRepository = reservaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    /** Consulta: el pago y la factura de una reserva. */
    public PagoResponse obtener(Long idReserva) {
        buscarReserva(idReserva);
        return transaccionRepository.findById(idReserva)
                .map(PagoResponse::from)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La reserva " + idReserva + " no tiene pago registrado"));
    }

    /** Registro: paga una reserva PENDIENTE, genera la factura y la confirma. */
    @Transactional
    public PagoResponse pagar(Long idReserva, PagoRequest request) {
        Reserva reserva = buscarReserva(idReserva);
        if (reserva.getTransaccion() != null) {
            throw new ConflictoException("La reserva ya tiene un pago registrado");
        }
        if (reserva.getEstadoReserva() != EstadoReserva.PENDIENTE) {
            throw new ConflictoException("Solo se puede pagar una reserva PENDIENTE; esta está "
                    + reserva.getEstadoReserva());
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setReserva(reserva);
        transaccion.setMontoPagado(reserva.calcularValorTotal());
        transaccion.setMetodoPago(request.metodoPago());
        transaccion.setEstadoTransaccion(EstadoTransaccion.APROBADA);
        transaccion.setFechaPago(LocalDateTime.now());

        Factura factura = new Factura();
        factura.setTransaccion(transaccion);
        transaccion.setFactura(factura);

        reserva.setTransaccion(transaccion);
        reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);
        return PagoResponse.from(transaccionRepository.save(transaccion));
    }

    private Reserva buscarReserva(Long idReserva) {
        return reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la reserva con id " + idReserva));
    }
}