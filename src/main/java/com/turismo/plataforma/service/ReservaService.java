package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.reserva.*;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.*;
import com.turismo.plataforma.model.enums.EstadoReserva;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.PlanRepository;
import com.turismo.plataforma.repository.ProgramacionViajeRepository;
import com.turismo.plataforma.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final ProgramacionViajeRepository programacionRepository;
    private final PlanRepository planRepository;

    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository,
                          ProgramacionViajeRepository programacionRepository, PlanRepository planRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.programacionRepository = programacionRepository;
        this.planRepository = planRepository;
    }

    /** Consulta: todas las reservas (resumen), o solo las de un cliente. */
    public List<ReservaResumenResponse> listar(Long idCliente) {
        List<Reserva> reservas;
        if (idCliente == null) {
            reservas = reservaRepository.listarConCliente();
        } else {
            buscarCliente(idCliente);
            reservas = reservaRepository.listarPorCliente(idCliente);
        }
        return reservas.stream()
                .map(ReservaResumenResponse::from)
                .toList();
    }

    /** Consulta: el detalle completo de una reserva. */
    public ReservaResponse obtener(Long id) {
        return ReservaResponse.from(buscarEntidad(id));
    }

    /** Registro: valida cliente, asientos, cupo de cada viaje y planes; guarda todo junto. */
    @Transactional
    public ReservaResponse crear(ReservaRequest request) {
        validarAsientosNoRepetidos(request.pasajeros());

        Reserva reserva = new Reserva();
        reserva.setCliente(buscarCliente(request.idCliente()));

        for (TrayectoRequest datos : request.trayectos()) {
            ProgramacionViaje programacion = buscarProgramacion(datos.idProgramacion());
            validarCupo(programacion, request.pasajeros().size());
            ReservaTrayecto trayecto = new ReservaTrayecto();
            trayecto.setProgramacion(programacion);
            trayecto.setOrdenTrayecto(datos.ordenTrayecto());
            trayecto.setTipoTrayecto(datos.tipoTrayecto());
            reserva.agregarTrayecto(trayecto);
        }

        for (PasajeroRequest datos : request.pasajeros()) {
            DetalleReserva detalle = new DetalleReserva();
            NombreCompleto nombre = detalle.getNombre();
            nombre.setPrimerNombre(datos.primerNombre().trim());
            nombre.setSegundoNombre(textoOpcional(datos.segundoNombre()));
            nombre.setPrimerApellido(datos.primerApellido().trim());
            nombre.setSegundoApellido(textoOpcional(datos.segundoApellido()));
            detalle.setIdentificacion(datos.identificacion().trim());
            detalle.setNumeroAsiento(datos.numeroAsiento().trim().toUpperCase());
            detalle.setPlan(buscarPlan(datos.idPlan()));
            reserva.agregarDetalle(detalle);
        }

        return ReservaResponse.from(reservaRepository.save(reserva));
    }

    /** Modificación: cancelar una reserva PENDIENTE (libera sus asientos). */
    @Transactional
    public ReservaResponse cancelar(Long id) {
        Reserva reserva = buscarEntidad(id);
        if (reserva.getEstadoReserva() != EstadoReserva.PENDIENTE) {
            throw new ConflictoException("Solo se puede cancelar una reserva PENDIENTE; esta está "
                    + reserva.getEstadoReserva());
        }
        reserva.setEstadoReserva(EstadoReserva.CANCELADA);
        return ReservaResponse.from(reserva);
    }

    /** Eliminación: solo reservas sin pago (borra también trayectos y pasajeros). */
    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = buscarEntidad(id);
        if (reserva.getTransaccion() != null) {
            throw new ConflictoException("Una reserva con pago registrado no se puede eliminar");
        }
        reservaRepository.delete(reserva);
        reservaRepository.flush();
    }

    private Reserva buscarEntidad(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la reserva con id " + id));
    }

    private Cliente buscarCliente(Long idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente con id " + idCliente));
    }

    private ProgramacionViaje buscarProgramacion(Long idProgramacion) {
        return programacionRepository.findById(idProgramacion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la programación con id " + idProgramacion));
    }

    private Plan buscarPlan(Long idPlan) {
        return planRepository.findById(idPlan)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el plan con id " + idPlan));
    }

    /** Regla: dos pasajeros de la misma reserva no pueden tener el mismo asiento. */
    private void validarAsientosNoRepetidos(List<PasajeroRequest> pasajeros) {
        Set<String> asientos = new HashSet<>();
        for (PasajeroRequest pasajero : pasajeros) {
            String asiento = pasajero.numeroAsiento().trim().toUpperCase();
            if (!asientos.add(asiento)) {
                throw new ConflictoException("El asiento " + asiento + " está repetido en la reserva");
            }
        }
    }

    /** Regla: el viaje debe tener asientos disponibles para todos los pasajeros. */
    private void validarCupo(ProgramacionViaje programacion, int pasajerosNuevos) {
        long ocupados = programacionRepository.contarAsientosOcupados(
                programacion.getIdProgramacion(), EstadoReserva.CANCELADA);
        long disponibles = programacion.getVehiculo().getCapacidad() - ocupados;
        if (pasajerosNuevos > disponibles) {
            throw new ConflictoException("El viaje " + programacion.getIdProgramacion()
                    + " solo tiene " + disponibles + " asientos disponibles");
        }
    }

    private String textoOpcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}