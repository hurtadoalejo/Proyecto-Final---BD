package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.reserva.*;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.*;
import com.turismo.plataforma.model.catalogo.EstadoReserva;
import com.turismo.plataforma.model.catalogo.TipoIdentificacion;
import com.turismo.plataforma.model.catalogo.TipoTrayecto;
import com.turismo.plataforma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Reservas: el cliente elige salidas (de la búsqueda) y registra los pasajeros.
 * Cada pasajero viaja en todas las salidas de la reserva con el mismo asiento y plan.
 */
@Service
@Transactional(readOnly = true)
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final OfertaRepository ofertaRepository;
    private final PlanRepository planRepository;
    private final ProgramacionViajeRepository programacionViajeRepository;
    private final CatalogoService catalogoService;

    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository,
                          EmpleadoRepository empleadoRepository, OfertaRepository ofertaRepository,
                          PlanRepository planRepository, ProgramacionViajeRepository programacionViajeRepository,
                          CatalogoService catalogoService) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.ofertaRepository = ofertaRepository;
        this.planRepository = planRepository;
        this.programacionViajeRepository = programacionViajeRepository;
        this.catalogoService = catalogoService;
    }

    /** Todas las reservas, o las de un cliente. */
    public List<ReservaResumenResponse> listar(Long idCliente) {
        return reservaRepository.listar(idCliente).stream()
                .map(ReservaResumenResponse::from)
                .toList();
    }

    public ReservaResponse obtener(Long id) {
        return ReservaResponse.from(buscarEntidad(id));
    }

    @Transactional
    public ReservaResponse crear(ReservaRequest request) {
        Cliente cliente = clienteRepository.findById(request.idCliente())
                .orElseThrow(() -> new SolicitudInvalidaException("No existe el cliente con id " + request.idCliente()));
        Empleado empleado = request.idEmpleado() == null ? null : empleadoRepository.findById(request.idEmpleado())
                .orElseThrow(() -> new SolicitudInvalidaException("No existe el empleado con id " + request.idEmpleado()));
        Oferta oferta = buscarOfertaVigente(request.idOferta());
        EstadoReserva pendiente = catalogoService.estadoReserva(EstadoReserva.PENDIENTE);

        validarPasajerosSinRepetir(request.pasajeros());
        List<ReservaViaje> viajes = prepararViajes(request.viajes(), request.pasajeros());
        List<DetalleReserva> detalles = prepararPasajeros(request.pasajeros());

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setEmpleado(empleado);
        reserva.setOferta(oferta);
        reserva.setEstadoReserva(pendiente);
        viajes.forEach(reserva::agregarViaje);
        detalles.forEach(reserva::agregarDetalle);
        return ReservaResponse.from(reservaRepository.save(reserva));
    }

    /** Solo se cancela una reserva PENDIENTE (sin pagar). Libera los asientos. */
    @Transactional
    public ReservaResponse cancelar(Long id) {
        Reserva reserva = buscarEntidad(id);
        if (!reserva.tieneEstado(EstadoReserva.PENDIENTE)) {
            throw new ConflictoException("Solo se pueden cancelar reservas PENDIENTES; esta está "
                    + reserva.getEstadoReserva().getNombreEstadoReserva());
        }
        reserva.setEstadoReserva(catalogoService.estadoReserva(EstadoReserva.CANCELADA));
        return ReservaResponse.from(reserva);
    }

    /** Elimina la reserva con sus viajes y pasajeros. No se puede si ya tiene pago. */
    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = buscarEntidad(id);
        if (reserva.getTransaccion() != null) {
            throw new ConflictoException("La reserva ya tiene un pago registrado; no se puede eliminar");
        }
        reservaRepository.delete(reserva);
        reservaRepository.flush();
    }

    private Oferta buscarOfertaVigente(Long idOferta) {
        if (idOferta == null) {
            return null;
        }
        Oferta oferta = ofertaRepository.findById(idOferta)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la oferta con id " + idOferta));
        if (!oferta.estaVigente(LocalDate.now())) {
            throw new SolicitudInvalidaException("La oferta '" + oferta.getTitulo() + "' no está vigente");
        }
        return oferta;
    }

    /** Dentro de la reserva no se repiten asientos ni identificaciones. */
    private static void validarPasajerosSinRepetir(List<PasajeroRequest> pasajeros) {
        Set<String> asientos = new HashSet<>();
        Set<String> identificaciones = new HashSet<>();
        for (PasajeroRequest pasajero : pasajeros) {
            if (!asientos.add(asiento(pasajero))) {
                throw new SolicitudInvalidaException("El asiento " + asiento(pasajero) + " está repetido");
            }
            if (!identificaciones.add(pasajero.numeroIdentificacion().trim())) {
                throw new SolicitudInvalidaException("El pasajero con identificación "
                        + pasajero.numeroIdentificacion().trim() + " está repetido");
            }
        }
    }

    /** Cada salida: existe, no ha partido, tiene cupo para todos y los asientos pedidos están libres. */
    private List<ReservaViaje> prepararViajes(List<ViajeReservaRequest> solicitados, List<PasajeroRequest> pasajeros) {
        Set<Long> vistos = new HashSet<>();
        List<ReservaViaje> viajes = new ArrayList<>();
        for (ViajeReservaRequest solicitado : solicitados) {
            Long idProgramacion = solicitado.idProgramacionViaje();
            if (!vistos.add(idProgramacion)) {
                throw new SolicitudInvalidaException("La salida " + idProgramacion + " está repetida");
            }
            ProgramacionViaje programacion = programacionViajeRepository.findById(idProgramacion)
                    .orElseThrow(() -> new SolicitudInvalidaException("No existe la salida con id " + idProgramacion));
            if (!programacion.getFechaHoraSalida().isAfter(LocalDateTime.now())) {
                throw new SolicitudInvalidaException("La salida " + idProgramacion + " ya partió");
            }
            Vehiculo vehiculo = programacion.getRuta().getVehiculo();
            if (vehiculo == null) {
                throw new SolicitudInvalidaException("La ruta de la salida " + idProgramacion + " no tiene vehículo");
            }
            long disponibles = vehiculo.getCapacidad() - programacionViajeRepository.contarAsientosOcupados(idProgramacion);
            if (disponibles < pasajeros.size()) {
                throw new ConflictoException("La salida " + idProgramacion + " solo tiene " + Math.max(0, disponibles)
                        + " asientos disponibles");
            }
            for (PasajeroRequest pasajero : pasajeros) {
                if (programacionViajeRepository.contarAsiento(idProgramacion, asiento(pasajero)) > 0) {
                    throw new ConflictoException("El asiento " + asiento(pasajero) + " ya está ocupado en la salida "
                            + idProgramacion);
                }
            }
            TipoTrayecto tipoTrayecto = catalogoService.tipoTrayecto(solicitado.tipoTrayecto());

            ReservaViaje viaje = new ReservaViaje();
            viaje.setProgramacionViaje(programacion);
            viaje.setTipoTrayecto(tipoTrayecto);
            viajes.add(viaje);
        }
        return viajes;
    }

    private List<DetalleReserva> prepararPasajeros(List<PasajeroRequest> pasajeros) {
        Map<Long, Plan> planes = new HashMap<>();
        List<DetalleReserva> detalles = new ArrayList<>();
        for (PasajeroRequest pasajero : pasajeros) {
            Plan plan = planes.computeIfAbsent(pasajero.idPlan(), idPlan -> planRepository.findById(idPlan)
                    .orElseThrow(() -> new SolicitudInvalidaException("No existe el plan con id " + idPlan)));
            TipoIdentificacion tipo = catalogoService.tipoIdentificacion(pasajero.tipoIdentificacion());

            DetalleReserva detalle = new DetalleReserva();
            detalle.getNombre().setPrimerNombre(pasajero.primerNombre().trim());
            detalle.getNombre().setSegundoNombre(opcional(pasajero.segundoNombre()));
            detalle.getNombre().setPrimerApellido(pasajero.primerApellido().trim());
            detalle.getNombre().setSegundoApellido(opcional(pasajero.segundoApellido()));
            detalle.setTipoIdentificacion(tipo);
            detalle.setIdentificacion(pasajero.numeroIdentificacion().trim());
            detalle.setNumeroAsiento(asiento(pasajero));
            detalle.setPlan(plan);
            detalles.add(detalle);
        }
        return detalles;
    }

    private static String asiento(PasajeroRequest pasajero) {
        return pasajero.numeroAsiento().trim().toUpperCase(Locale.ROOT);
    }

    private static String opcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private Reserva buscarEntidad(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la reserva con id " + id));
    }
}
