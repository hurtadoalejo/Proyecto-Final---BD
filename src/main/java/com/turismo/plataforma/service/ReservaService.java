package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.reserva.*;
import com.turismo.plataforma.exception.AccesoDenegadoException;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.*;
import com.turismo.plataforma.model.catalogo.EstadoReserva;
import com.turismo.plataforma.model.catalogo.EstadoTransaccion;
import com.turismo.plataforma.model.catalogo.TipoIdentificacion;
import com.turismo.plataforma.model.catalogo.TipoTrayecto;
import com.turismo.plataforma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Reservas: el cliente elige salidas (de la búsqueda) y registra los pasajeros.
 * Cada pasajero viaja en todas las salidas de la reserva con el mismo plan; por cada salida se
 * guarda un DetalleReserva con su asiento (ida y vuelta = dos detalles por pasajero).
 */
@Service
@Transactional(readOnly = true)
public class ReservaService {

    /** El cliente puede cancelar solo si faltan al menos estos días para el día del primer viaje. */
    public static final int DIAS_MINIMOS_CANCELACION = 5;

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    /** Historial de compras de un cliente, con viajes, pasajeros, valores y pago (de la más reciente a la más antigua). */
    public List<ReservaResponse> comprasDeCliente(Long idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new RecursoNoEncontradoException("No existe el cliente con id " + idCliente);
        }
        return reservaRepository.listar(idCliente).stream()
                .map(ReservaResponse::from)
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
        if (oferta != null && reservaRepository.ofertasUsadasPorCliente(cliente.getIdUsuario())
                .contains(oferta.getIdOferta())) {
            throw new ConflictoException("El cliente ya usó la oferta '" + oferta.getTitulo()
                    + "' en otra reserva; cada oferta se puede usar una sola vez");
        }
        EstadoReserva pendiente = catalogoService.estadoReserva(EstadoReserva.PENDIENTE);

        validarPasajerosSinRepetir(request.pasajeros());
        List<ViajePreparado> viajes = prepararViajes(request.viajes(), request.pasajeros());

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setEmpleado(empleado);
        reserva.setOferta(oferta);
        reserva.setEstadoReserva(pendiente);
        viajes.forEach(v -> reserva.agregarViaje(v.viaje()));
        for (ViajePreparado viaje : viajes) {
            List<String> asientos = asignarAsientos(request.pasajeros(), viaje);
            prepararPasajeros(request.pasajeros(), asientos, viaje.viaje().getProgramacionViaje())
                    .forEach(reserva::agregarDetalle);
        }
        return ReservaResponse.from(reservaRepository.save(reserva));
    }

    /**
     * El cliente cancela una de sus reservas (pendiente o pagada) si faltan 5 días o más para el
     * día del primer viaje. Libera los asientos y la oferta; si estaba pagada, el pago queda REEMBOLSADA.
     */
    @Transactional
    public ReservaResponse cancelar(Long id, Long idCliente) {
        Reserva reserva = buscarEntidad(id);
        if (idCliente == null || !reserva.getCliente().getIdUsuario().equals(idCliente)) {
            throw new AccesoDenegadoException("Solo el cliente dueño de la reserva puede cancelarla");
        }
        if (reserva.tieneEstado(EstadoReserva.CANCELADA)) {
            throw new ConflictoException("La reserva #" + id + " ya está cancelada");
        }
        LocalDateTime primeraSalida = reserva.primeraSalida();
        if (primeraSalida != null) {
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), primeraSalida.toLocalDate());
            if (dias < DIAS_MINIMOS_CANCELACION) {
                throw new ConflictoException("Solo se puede cancelar con " + DIAS_MINIMOS_CANCELACION
                        + " días o más de anticipación. El viaje es el " + primeraSalida.format(FECHA)
                        + (dias <= 0 ? " y ya no se puede cancelar." : " y faltan " + dias + (dias == 1 ? " día." : " días.")));
            }
        }
        reserva.setEstadoReserva(catalogoService.estadoReserva(EstadoReserva.CANCELADA));
        Transaccion transaccion = reserva.getTransaccion();
        if (transaccion != null
                && transaccion.getEstadoTransaccion().getNombreEstadoTransaccion().equals(EstadoTransaccion.APROBADA)) {
            transaccion.setEstadoTransaccion(catalogoService.estadoTransaccion(EstadoTransaccion.REEMBOLSADA));
        }
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

    /** Dentro de la reserva no se repiten asientos (los que se piden) ni identificaciones. */
    private static void validarPasajerosSinRepetir(List<PasajeroRequest> pasajeros) {
        Set<String> asientos = new HashSet<>();
        Set<String> identificaciones = new HashSet<>();
        for (PasajeroRequest pasajero : pasajeros) {
            if (asiento(pasajero) != null && !asientos.add(asiento(pasajero))) {
                throw new SolicitudInvalidaException("El asiento " + asiento(pasajero) + " está repetido");
            }
            if (!identificaciones.add(pasajero.numeroIdentificacion().trim())) {
                throw new SolicitudInvalidaException("El pasajero con identificación "
                        + pasajero.numeroIdentificacion().trim() + " está repetido");
            }
        }
    }

    /** Un viaje de la reserva con los asientos ya ocupados en esa salida y la capacidad del vehículo. */
    private record ViajePreparado(ReservaViaje viaje, Set<String> asientosOcupados, int capacidad) {
    }

    /** Cada salida: existe, no ha partido, tiene cupo para todos y los asientos pedidos están libres. */
    private List<ViajePreparado> prepararViajes(List<ViajeReservaRequest> solicitados, List<PasajeroRequest> pasajeros) {
        Set<Long> vistos = new HashSet<>();
        List<ViajePreparado> viajes = new ArrayList<>();
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
            Set<String> ocupadosSalida = new HashSet<>(programacionViajeRepository.listarAsientosOcupados(idProgramacion));
            for (PasajeroRequest pasajero : pasajeros) {
                if (asiento(pasajero) != null && ocupadosSalida.contains(asiento(pasajero))) {
                    throw new ConflictoException("El asiento " + asiento(pasajero) + " ya está ocupado en la salida "
                            + idProgramacion);
                }
            }
            TipoTrayecto tipoTrayecto = catalogoService.tipoTrayecto(solicitado.tipoTrayecto());

            ReservaViaje viaje = new ReservaViaje();
            viaje.setProgramacionViaje(programacion);
            viaje.setTipoTrayecto(tipoTrayecto);
            viajes.add(new ViajePreparado(viaje, ocupadosSalida, vehiculo.getCapacidad()));
        }
        return viajes;
    }

    /**
     * Asientos de los pasajeros en una salida: respeta el asiento que se pidió y a los demás les da
     * el primer número libre de esa salida (1, 2, 3... hasta la capacidad del vehículo).
     */
    private static List<String> asignarAsientos(List<PasajeroRequest> pasajeros, ViajePreparado viaje) {
        Set<String> tomados = new HashSet<>(viaje.asientosOcupados());
        pasajeros.stream().map(ReservaService::asiento).filter(a -> a != null).forEach(tomados::add);
        List<String> asientos = new ArrayList<>();
        int siguiente = 1;
        for (PasajeroRequest pasajero : pasajeros) {
            String asiento = asiento(pasajero);
            if (asiento == null) {
                while (tomados.contains(String.valueOf(siguiente))) {
                    siguiente++;
                }
                if (siguiente > viaje.capacidad()) {
                    throw new ConflictoException("No quedan asientos libres en la salida "
                            + viaje.viaje().getProgramacionViaje().getIdProgramacionViaje());
                }
                asiento = String.valueOf(siguiente);
                tomados.add(asiento);
            }
            asientos.add(asiento);
        }
        return asientos;
    }

    private List<DetalleReserva> prepararPasajeros(List<PasajeroRequest> pasajeros, List<String> asientos,
                                                   ProgramacionViaje programacion) {
        Map<Long, Plan> planes = new HashMap<>();
        List<DetalleReserva> detalles = new ArrayList<>();
        for (int i = 0; i < pasajeros.size(); i++) {
            PasajeroRequest pasajero = pasajeros.get(i);
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
            detalle.setNumeroAsiento(asientos.get(i));
            detalle.setPlan(plan);
            detalle.setProgramacionViaje(programacion);
            detalles.add(detalle);
        }
        return detalles;
    }

    /** Asiento pedido en mayúsculas, o null si no se pidió. */
    private static String asiento(PasajeroRequest pasajero) {
        String asiento = pasajero.numeroAsiento();
        return asiento == null || asiento.isBlank() ? null : asiento.trim().toUpperCase(Locale.ROOT);
    }

    private static String opcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private Reserva buscarEntidad(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la reserva con id " + id));
    }
}
