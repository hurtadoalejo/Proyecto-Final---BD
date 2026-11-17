package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.programacion.*;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.ProgramacionViaje;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.model.Vehiculo;
import com.turismo.plataforma.repository.ProgramacionViajeRepository;
import com.turismo.plataforma.repository.RutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/** Salidas (fecha y hora) de cada ruta, puntuales o repetidas cada cierto número de horas. */
@Service
@Transactional(readOnly = true)
public class ProgramacionViajeService {

    /** Máximo de días por consulta del listado. */
    private static final int MAX_DIAS_LISTADO = 31;
    /** Máximo de salidas que crea una sola programación recurrente. */
    private static final int MAX_SALIDAS_RECURRENTES = 1000;
    /** Horas mínimas entre la llegada de un viaje y la siguiente salida del mismo vehículo. */
    public static final int HORAS_DESCANSO = 12;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ProgramacionViajeRepository programacionViajeRepository;
    private final RutaRepository rutaRepository;

    public ProgramacionViajeService(ProgramacionViajeRepository programacionViajeRepository,
                                    RutaRepository rutaRepository) {
        this.programacionViajeRepository = programacionViajeRepository;
        this.rutaRepository = rutaRepository;
    }

    /** Salidas entre desde y hasta (incluidos). Por defecto: hoy y los 7 días siguientes. */
    public List<ProgramacionResponse> listar(Long idRuta, LocalDate desde, LocalDate hasta) {
        LocalDate inicio = desde == null ? LocalDate.now() : desde;
        LocalDate fin = hasta == null ? inicio.plusDays(7) : hasta;
        if (fin.isBefore(inicio)) {
            throw new SolicitudInvalidaException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }
        if (ChronoUnit.DAYS.between(inicio, fin) > MAX_DIAS_LISTADO) {
            throw new SolicitudInvalidaException("Consulta máximo " + MAX_DIAS_LISTADO + " días a la vez");
        }
        return programacionViajeRepository.listar(idRuta, inicio.atStartOfDay(), fin.plusDays(1).atStartOfDay())
                .stream()
                .map(ProgramacionResponse::from)
                .toList();
    }

    public ProgramacionResponse obtener(Long id) {
        return ProgramacionResponse.from(buscarEntidad(id));
    }

    /** Atributo derivado: asientos disponibles = capacidad del vehículo - pasajeros reservados. */
    public DisponibilidadResponse disponibilidad(Long id) {
        ProgramacionViaje programacion = buscarEntidad(id);
        Vehiculo vehiculo = programacion.getRuta().getVehiculo();
        int capacidad = vehiculo == null ? 0 : vehiculo.getCapacidad();
        long ocupados = programacionViajeRepository.contarAsientosOcupados(id);
        List<String> asientos = programacionViajeRepository.listarAsientosOcupados(id).stream().sorted().toList();
        return new DisponibilidadResponse(id, capacidad, ocupados, Math.max(0, capacidad - ocupados), asientos);
    }

    @Transactional
    public ProgramacionResponse crear(ProgramacionRequest request) {
        Ruta ruta = buscarRutaConVehiculo(request.idRuta());
        LocalDateTime salida = request.fechaHoraSalida().truncatedTo(ChronoUnit.MINUTES);
        if (programacionViajeRepository.existsByRutaIdRutaAndFechaHoraSalida(ruta.getIdRuta(), salida)) {
            throw new ConflictoException("La ruta " + ruta.getIdRuta() + " ya tiene una salida el " + salida.format(FORMATO));
        }
        validarVehiculoLibre(ruta.getVehiculo(), ruta.getDuracionEstimada(), salida, null);
        ProgramacionViaje programacion = new ProgramacionViaje();
        programacion.setRuta(ruta);
        programacion.setFechaHoraSalida(salida);
        return ProgramacionResponse.from(programacionViajeRepository.save(programacion));
    }

    /**
     * Repite la ruta: una salida en fechaHoraInicio y otra cada "cadaHoras" horas hasta fechaHoraFin.
     * Las que ya existían se omiten y las que chocan con otro viaje del vehículo se cuentan como conflictos
     * (no es error). cadaHoras debe dejar al vehículo 12 horas de descanso entre una salida y la siguiente.
     */
    @Transactional
    public ProgramacionRecurrenteResponse crearRecurrente(ProgramacionRecurrenteRequest request) {
        LocalDateTime inicio = request.fechaHoraInicio().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime fin = request.fechaHoraFin();
        if (fin.isBefore(inicio)) {
            throw new SolicitudInvalidaException("La fecha límite no puede ser anterior a la primera salida");
        }
        long cantidad = ChronoUnit.HOURS.between(inicio, fin) / request.cadaHoras() + 1;
        if (cantidad > MAX_SALIDAS_RECURRENTES) {
            throw new SolicitudInvalidaException("Se crearían " + cantidad + " salidas; el máximo por solicitud es "
                    + MAX_SALIDAS_RECURRENTES + ". Reduce el periodo o aumenta cadaHoras");
        }
        Ruta ruta = buscarRutaConVehiculo(request.idRuta());
        int minutosMinimos = ruta.getDuracionEstimada() + HORAS_DESCANSO * 60;
        if (request.cadaHoras() * 60 < minutosMinimos) {
            throw new SolicitudInvalidaException("Esta ruta dura " + ruta.getDuracionEstimada()
                    + " minutos y el vehículo necesita " + HORAS_DESCANSO + " horas de descanso: la frecuencia "
                    + "mínima es cada " + (int) Math.ceil(minutosMinimos / 60.0) + " horas");
        }

        List<ProgramacionViaje> nuevas = new ArrayList<>();
        int omitidas = 0;
        int conflictos = 0;
        for (LocalDateTime salida = inicio; !salida.isAfter(fin); salida = salida.plusHours(request.cadaHoras())) {
            if (programacionViajeRepository.existsByRutaIdRutaAndFechaHoraSalida(ruta.getIdRuta(), salida)) {
                omitidas++;
                continue;
            }
            if (buscarCruce(ruta.getVehiculo(), ruta.getDuracionEstimada(), salida, null) != null) {
                conflictos++;
                continue;
            }
            ProgramacionViaje programacion = new ProgramacionViaje();
            programacion.setRuta(ruta);
            programacion.setFechaHoraSalida(salida);
            nuevas.add(programacion);
        }
        List<ProgramacionResponse> salidas = programacionViajeRepository.saveAll(nuevas).stream()
                .map(ProgramacionResponse::from)
                .toList();
        return new ProgramacionRecurrenteResponse(salidas.size(), omitidas, conflictos, salidas);
    }

    /** Cambia la ruta o la hora de una salida que todavía no tiene pasajeros. */
    @Transactional
    public ProgramacionResponse actualizar(Long id, ProgramacionRequest request) {
        ProgramacionViaje programacion = buscarEntidad(id);
        Ruta ruta = buscarRutaConVehiculo(request.idRuta());
        LocalDateTime salida = request.fechaHoraSalida().truncatedTo(ChronoUnit.MINUTES);
        if (programacionViajeRepository.existsByRutaIdRutaAndFechaHoraSalidaAndIdProgramacionViajeNot(
                ruta.getIdRuta(), salida, id)) {
            throw new ConflictoException("La ruta " + ruta.getIdRuta() + " ya tiene una salida el " + salida.format(FORMATO));
        }
        if (programacionViajeRepository.contarAsientosOcupados(id) > 0) {
            throw new ConflictoException("La salida ya tiene pasajeros reservados; no se puede modificar");
        }
        validarVehiculoLibre(ruta.getVehiculo(), ruta.getDuracionEstimada(), salida, id);
        programacion.setRuta(ruta);
        programacion.setFechaHoraSalida(salida);
        return ProgramacionResponse.from(programacion);
    }

    /** Falla con 409 si alguna reserva incluye esta salida. */
    @Transactional
    public void eliminar(Long id) {
        programacionViajeRepository.delete(buscarEntidad(id));
        programacionViajeRepository.flush();
    }

    /**
     * Capa de seguridad contra cruces de horario: el vehículo no puede tener otro viaje que empiece antes
     * de 12 horas después de que este llegue, ni que llegue menos de 12 horas antes de que este salga.
     * idExcluir = la salida que se está modificando (null al crear).
     */
    public void validarVehiculoLibre(Vehiculo vehiculo, int duracionMinutos, LocalDateTime salida, Long idExcluir) {
        ProgramacionViaje cruce = buscarCruce(vehiculo, duracionMinutos, salida, idExcluir);
        if (cruce != null) {
            Ruta otra = cruce.getRuta();
            throw new ConflictoException("El vehículo " + vehiculo.getCodigoIdentificacion() + " ya tiene la salida "
                    + cruce.getIdProgramacionViaje() + " (" + otra.getCiudadOrigen().getNombreCiudad() + " → "
                    + otra.getCiudadDestino().getNombreCiudad() + ", " + cruce.getFechaHoraSalida().format(FORMATO)
                    + " a " + cruce.calcularLlegada().format(FORMATO) + "). Entre un viaje y otro del mismo vehículo "
                    + "deben pasar al menos " + HORAS_DESCANSO + " horas");
        }
    }

    private ProgramacionViaje buscarCruce(Vehiculo vehiculo, int duracionMinutos, LocalDateTime salida,
                                          Long idExcluir) {
        LocalDateTime limiteSuperior = salida.plusMinutes(duracionMinutos).plusHours(HORAS_DESCANSO);
        List<ProgramacionViaje> cruces = programacionViajeRepository.buscarCruceVehiculo(vehiculo.getIdVehiculo(),
                idExcluir == null ? -1L : idExcluir, salida, limiteSuperior, HORAS_DESCANSO * 60);
        return cruces.isEmpty() ? null : cruces.getFirst();
    }

    private Ruta buscarRutaConVehiculo(Long idRuta) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la ruta con id " + idRuta));
        if (ruta.getVehiculo() == null) {
            throw new SolicitudInvalidaException("La ruta " + idRuta + " no tiene vehículo asignado");
        }
        return ruta;
    }

    private ProgramacionViaje buscarEntidad(Long id) {
        return programacionViajeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la programación con id " + id));
    }
}
