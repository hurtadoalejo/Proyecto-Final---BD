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
 * Cada pasajero viaja en todas las salidas de la reserva con el mismo asiento y plan.
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
    private final ReservaViajeRepository reservaViajeRepository;
    private final CatalogoService catalogoService;

    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository,
                          EmpleadoRepository empleadoRepository, OfertaRepository ofertaRepository,
                          PlanRepository planRepository, ProgramacionViajeRepository programacionViajeRepository,
                          ReservaViajeRepository reservaViajeRepository, CatalogoService catalogoService) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.ofertaRepository = ofertaRepository;
        this.planRepository = planRepository;
        this.programacionViajeRepository = programacionViajeRepository;
        this.reservaViajeRepository = reservaViajeRepository;
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
        Viajes viajes = prepararViajes(request.viajes(), request.pasajeros());
        List<String> asientos = asignarAsientos(request.pasajeros(), viajes);
        List<DetalleReserva> detalles = prepararPasajeros(request.pasajeros(), asientos);

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setEmpleado(empleado);
        reserva.setOferta(oferta);
        reserva.setEstadoReserva(pendiente);
        viajes.lista().forEach(reserva::agregarViaje);
        detalles.forEach(reserva::agregarDetalle);
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

    // ------------------------------------------------------------------ cambio de fecha

    /**
     * Salidas de la misma ruta a las que el cliente puede mover un viaje de su reserva: futuras,
     * con cupo para todos sus pasajeros y que respetan el orden ida/regreso.
     */
    @Transactional(readOnly = true)
    public List<AlternativaFechaResponse> alternativasDeFecha(Long idReserva, Long idProgramacionActual, Long idCliente) {
        Reserva reserva = buscarEntidad(idReserva);
        ReservaViaje viaje = validarCambioPermitido(reserva, idProgramacionActual, idCliente);
        ProgramacionViaje actual = viaje.getProgramacionViaje();
        Ventana ventana = ventana(reserva, viaje);
        LocalDateTime desde = LocalDateTime.now();
        LocalDateTime hasta = ventana.hasta() != null ? ventana.hasta().plusSeconds(1) : desde.plusYears(1);
        Set<Long> salidasDeLaReserva = new HashSet<>();
        reserva.getViajes().forEach(v -> salidasDeLaReserva.add(v.getProgramacionViaje().getIdProgramacionViaje()));
        int pasajeros = reserva.getDetalles().size();
        int capacidad = actual.getRuta().getVehiculo().getCapacidad();

        return programacionViajeRepository.listar(actual.getRuta().getIdRuta(), desde, hasta).stream()
                .filter(p -> !salidasDeLaReserva.contains(p.getIdProgramacionViaje()))
                .filter(p -> ventana.permite(p.getFechaHoraSalida()))
                .map(p -> new AlternativaFechaResponse(p.getIdProgramacionViaje(), p.getFechaHoraSalida(),
                        p.calcularLlegada(),
                        capacidad - programacionViajeRepository.contarAsientosOcupados(p.getIdProgramacionViaje())))
                .filter(a -> a.asientosDisponibles() >= pasajeros)
                .toList();
    }

    /**
     * El cliente mueve un viaje de su reserva a otra salida de la misma ruta (solo cambia la fecha:
     * mismo trayecto, mismo precio, mismos pasajeros, plan y oferta). Solo si faltan 5 días o más
     * para la salida actual. La fila de ReservaViaje queda apuntando a la nueva salida; si el asiento
     * de un pasajero ya está ocupado allí, se le asigna el primer número libre en todos sus viajes.
     */
    @Transactional
    public ReservaResponse cambiarFecha(Long idReserva, Long idProgramacionActual, Long idProgramacionNueva,
                                        Long idCliente) {
        Reserva reserva = buscarEntidad(idReserva);
        ReservaViaje viaje = validarCambioPermitido(reserva, idProgramacionActual, idCliente);
        ProgramacionViaje actual = viaje.getProgramacionViaje();
        if (idProgramacionNueva.equals(idProgramacionActual)) {
            throw new SolicitudInvalidaException("La nueva salida es la misma que ya tienes");
        }
        ProgramacionViaje nueva = programacionViajeRepository.findById(idProgramacionNueva)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la salida con id " + idProgramacionNueva));
        if (!nueva.getRuta().getIdRuta().equals(actual.getRuta().getIdRuta())) {
            throw new SolicitudInvalidaException("Solo se puede cambiar la fecha: la nueva salida debe ser de la misma "
                    + "ruta (" + actual.getRuta().getCiudadOrigen().getNombreCiudad() + " → "
                    + actual.getRuta().getCiudadDestino().getNombreCiudad() + ")");
        }
        if (!nueva.getFechaHoraSalida().isAfter(LocalDateTime.now())) {
            throw new SolicitudInvalidaException("La salida elegida ya partió");
        }
        boolean yaEnLaReserva = reserva.getViajes().stream()
                .anyMatch(v -> v.getProgramacionViaje().getIdProgramacionViaje().equals(idProgramacionNueva));
        if (yaEnLaReserva) {
            throw new ConflictoException("Esa salida ya hace parte de la reserva");
        }
        Ventana ventana = ventana(reserva, viaje);
        if (!ventana.permite(nueva.getFechaHoraSalida())) {
            throw new ConflictoException(ventana.mensaje());
        }
        int pasajeros = reserva.getDetalles().size();
        long disponibles = nueva.getRuta().getVehiculo().getCapacidad()
                - programacionViajeRepository.contarAsientosOcupados(idProgramacionNueva);
        if (disponibles < pasajeros) {
            throw new ConflictoException("La salida del " + nueva.getFechaHoraSalida().format(FECHA) + " solo tiene "
                    + Math.max(0, disponibles) + " asientos disponibles y la reserva tiene " + pasajeros + " pasajeros");
        }

        reasignarAsientosOcupados(reserva, viaje, nueva);
        reservaViajeRepository.cambiarSalida(idReserva, idProgramacionActual, idProgramacionNueva);
        return ReservaResponse.from(buscarEntidad(idReserva));
    }

    /** Reglas comunes: dueño, reserva activa, el viaje es de la reserva y faltan 5 días o más para él. */
    private ReservaViaje validarCambioPermitido(Reserva reserva, Long idProgramacionActual, Long idCliente) {
        if (idCliente == null || !reserva.getCliente().getIdUsuario().equals(idCliente)) {
            throw new AccesoDenegadoException("Solo el cliente dueño de la reserva puede cambiar sus fechas");
        }
        if (reserva.tieneEstado(EstadoReserva.CANCELADA)) {
            throw new ConflictoException("La reserva #" + reserva.getIdReserva() + " está cancelada");
        }
        ReservaViaje viaje = reserva.getViajes().stream()
                .filter(v -> v.getProgramacionViaje().getIdProgramacionViaje().equals(idProgramacionActual))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("La salida " + idProgramacionActual
                        + " no hace parte de la reserva #" + reserva.getIdReserva()));
        LocalDateTime salida = viaje.getProgramacionViaje().getFechaHoraSalida();
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), salida.toLocalDate());
        if (dias < DIAS_MINIMOS_CANCELACION) {
            throw new ConflictoException("Solo se puede cambiar la fecha con " + DIAS_MINIMOS_CANCELACION
                    + " días o más de anticipación. Este viaje sale el " + salida.format(FECHA)
                    + (dias <= 0 ? " y ya no se puede cambiar." : " y faltan " + dias + (dias == 1 ? " día." : " días.")));
        }
        return viaje;
    }

    /**
     * Rango permitido para la nueva salida según los otros viajes de la reserva: la ida debe llegar
     * antes de que salga el regreso, y el regreso debe salir después de que llegue la ida.
     */
    private record Ventana(LocalDateTime desde, LocalDateTime hasta, String mensaje) {
        boolean permite(LocalDateTime salida) {
            return (desde == null || !salida.isBefore(desde)) && (hasta == null || !salida.isAfter(hasta));
        }
    }

    private Ventana ventana(Reserva reserva, ReservaViaje viaje) {
        ProgramacionViaje actual = viaje.getProgramacionViaje();
        int duracion = actual.getRuta().getDuracionEstimada();
        LocalDateTime desde = null;
        LocalDateTime hasta = null;
        String mensaje = null;
        for (ReservaViaje otro : reserva.getViajes()) {
            ProgramacionViaje p = otro.getProgramacionViaje();
            if (p.getIdProgramacionViaje().equals(actual.getIdProgramacionViaje())) {
                continue;
            }
            if (p.getFechaHoraSalida().isAfter(actual.getFechaHoraSalida())) {
                LocalDateTime limite = p.getFechaHoraSalida().minusMinutes(duracion);
                if (hasta == null || limite.isBefore(hasta)) {
                    hasta = limite;
                    mensaje = "La ida debe llegar antes de que salga el regreso (" + p.getFechaHoraSalida().format(FECHA)
                            + "). Elige una fecha anterior o cambia primero el regreso.";
                }
            } else {
                LocalDateTime limite = p.calcularLlegada();
                if (desde == null || limite.isAfter(desde)) {
                    desde = limite;
                    mensaje = "El regreso debe salir después de que llegue la ida (" + limite.format(FECHA)
                            + "). Elige una fecha posterior o cambia primero la ida.";
                }
            }
        }
        return new Ventana(desde, hasta, mensaje);
    }

    /**
     * Cada pasajero conserva su número de asiento (es el mismo en todos los viajes). Si en la nueva
     * salida ese número ya está ocupado, recibe el primer número libre en la nueva salida y en los
     * demás viajes de la reserva.
     */
    private void reasignarAsientosOcupados(Reserva reserva, ReservaViaje viaje, ProgramacionViaje nueva) {
        Set<String> ocupadosNueva = new HashSet<>(
                programacionViajeRepository.listarAsientosOcupados(nueva.getIdProgramacionViaje()));
        List<DetalleReserva> aMover = reserva.getDetalles().stream()
                .filter(d -> ocupadosNueva.contains(d.getNumeroAsiento().toUpperCase()))
                .toList();
        if (aMover.isEmpty()) {
            return;
        }
        Set<String> propios = new HashSet<>();
        reserva.getDetalles().forEach(d -> propios.add(d.getNumeroAsiento().toUpperCase()));

        Set<String> tomados = new HashSet<>(ocupadosNueva);
        int capacidad = nueva.getRuta().getVehiculo().getCapacidad();
        for (ReservaViaje otro : reserva.getViajes()) {
            if (otro == viaje) {
                continue;
            }
            Long idOtra = otro.getProgramacionViaje().getIdProgramacionViaje();
            programacionViajeRepository.listarAsientosOcupados(idOtra).stream()
                    .filter(a -> !propios.contains(a))
                    .forEach(tomados::add);
            capacidad = Math.min(capacidad, otro.getProgramacionViaje().getRuta().getVehiculo().getCapacidad());
        }
        reserva.getDetalles().stream()
                .filter(d -> !aMover.contains(d))
                .forEach(d -> tomados.add(d.getNumeroAsiento().toUpperCase()));

        int siguiente = 1;
        for (DetalleReserva detalle : aMover) {
            while (tomados.contains(String.valueOf(siguiente))) {
                siguiente++;
            }
            if (siguiente > capacidad) {
                throw new ConflictoException("No hay un asiento libre a la vez en la nueva salida y en el otro viaje "
                        + "de la reserva; elige otra fecha");
            }
            detalle.setNumeroAsiento(String.valueOf(siguiente));
            tomados.add(String.valueOf(siguiente));
        }
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

    /** Viajes de la reserva, asientos ya ocupados en cualquiera de sus salidas y la menor capacidad. */
    private record Viajes(List<ReservaViaje> lista, Set<String> asientosOcupados, int capacidadMinima) {
    }

    /** Cada salida: existe, no ha partido, tiene cupo para todos y los asientos pedidos están libres. */
    private Viajes prepararViajes(List<ViajeReservaRequest> solicitados, List<PasajeroRequest> pasajeros) {
        Set<Long> vistos = new HashSet<>();
        List<ReservaViaje> viajes = new ArrayList<>();
        Set<String> ocupados = new HashSet<>();
        int capacidadMinima = Integer.MAX_VALUE;
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
            List<String> ocupadosSalida = programacionViajeRepository.listarAsientosOcupados(idProgramacion);
            for (PasajeroRequest pasajero : pasajeros) {
                if (asiento(pasajero) != null && ocupadosSalida.contains(asiento(pasajero))) {
                    throw new ConflictoException("El asiento " + asiento(pasajero) + " ya está ocupado en la salida "
                            + idProgramacion);
                }
            }
            ocupados.addAll(ocupadosSalida);
            capacidadMinima = Math.min(capacidadMinima, vehiculo.getCapacidad());
            TipoTrayecto tipoTrayecto = catalogoService.tipoTrayecto(solicitado.tipoTrayecto());

            ReservaViaje viaje = new ReservaViaje();
            viaje.setProgramacionViaje(programacion);
            viaje.setTipoTrayecto(tipoTrayecto);
            viajes.add(viaje);
        }
        return new Viajes(viajes, ocupados, capacidadMinima);
    }

    /**
     * Respeta el asiento que se pidió; a los pasajeros sin asiento les da el primer número libre
     * (1, 2, 3... hasta la capacidad del vehículo) en todas las salidas de la reserva.
     */
    private static List<String> asignarAsientos(List<PasajeroRequest> pasajeros, Viajes viajes) {
        Set<String> tomados = new HashSet<>(viajes.asientosOcupados());
        pasajeros.stream().map(ReservaService::asiento).filter(a -> a != null).forEach(tomados::add);
        List<String> asientos = new ArrayList<>();
        int siguiente = 1;
        for (PasajeroRequest pasajero : pasajeros) {
            String asiento = asiento(pasajero);
            if (asiento == null) {
                while (tomados.contains(String.valueOf(siguiente))) {
                    siguiente++;
                }
                if (siguiente > viajes.capacidadMinima()) {
                    throw new ConflictoException("No quedan asientos libres en todas las salidas de la reserva");
                }
                asiento = String.valueOf(siguiente);
                tomados.add(asiento);
            }
            asientos.add(asiento);
        }
        return asientos;
    }

    private List<DetalleReserva> prepararPasajeros(List<PasajeroRequest> pasajeros, List<String> asientos) {
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
