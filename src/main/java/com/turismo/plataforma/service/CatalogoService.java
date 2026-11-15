package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.catalogo.CatalogoItem;
import com.turismo.plataforma.dto.catalogo.CiudadResponse;
import com.turismo.plataforma.dto.catalogo.IndicativoResponse;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.catalogo.*;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.model.ubicacion.Departamento;
import com.turismo.plataforma.model.ubicacion.Pais;
import com.turismo.plataforma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tablas catálogo (Rol, TipoIdentificacion, Cargo, estados...) y ubicación (País, Departamento, Ciudad).
 * - Listas para los desplegables del frontend.
 * - Búsqueda por nombre de los valores que llegan en los JSON (ej.: "CC", "AVION", "IDA").
 */
@Service
@Transactional(readOnly = true)
public class CatalogoService {

    private final RolRepository rolRepository;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;
    private final CargoRepository cargoRepository;
    private final EstadoNotificacionRepository estadoNotificacionRepository;
    private final EstadoReservaRepository estadoReservaRepository;
    private final EstadoTransaccionRepository estadoTransaccionRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final TipoTransporteRepository tipoTransporteRepository;
    private final TipoTrayectoRepository tipoTrayectoRepository;
    private final PaisRepository paisRepository;
    private final DepartamentoRepository departamentoRepository;
    private final CiudadRepository ciudadRepository;

    public CatalogoService(RolRepository rolRepository,
                           TipoIdentificacionRepository tipoIdentificacionRepository,
                           CargoRepository cargoRepository,
                           EstadoNotificacionRepository estadoNotificacionRepository,
                           EstadoReservaRepository estadoReservaRepository,
                           EstadoTransaccionRepository estadoTransaccionRepository,
                           MetodoPagoRepository metodoPagoRepository,
                           TipoTransporteRepository tipoTransporteRepository,
                           TipoTrayectoRepository tipoTrayectoRepository,
                           PaisRepository paisRepository,
                           DepartamentoRepository departamentoRepository,
                           CiudadRepository ciudadRepository) {
        this.rolRepository = rolRepository;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
        this.cargoRepository = cargoRepository;
        this.estadoNotificacionRepository = estadoNotificacionRepository;
        this.estadoReservaRepository = estadoReservaRepository;
        this.estadoTransaccionRepository = estadoTransaccionRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.tipoTransporteRepository = tipoTransporteRepository;
        this.tipoTrayectoRepository = tipoTrayectoRepository;
        this.paisRepository = paisRepository;
        this.departamentoRepository = departamentoRepository;
        this.ciudadRepository = ciudadRepository;
    }

    // ------------------------------------------------------------------ listas

    public List<CatalogoItem> paises() {
        return paisRepository.findAllByOrderByNombrePais().stream()
                .map(p -> new CatalogoItem(p.getIdPais(), p.getNombrePais()))
                .toList();
    }

    /** Indicativos telefónicos (valor a enviar: indicativo, ej. "+57"). */
    public List<IndicativoResponse> indicativos() {
        return paisRepository.findAllByOrderByNombrePais().stream()
                .filter(p -> p.getIndicativo() != null)
                .map(p -> new IndicativoResponse(p.getIdPais(), p.getNombrePais(), "+" + p.getIndicativo()))
                .toList();
    }

    public List<CatalogoItem> departamentos(Long idPais) {
        return departamentoRepository.findByPaisIdPaisOrderByNombreDepartamento(idPais).stream()
                .map(d -> new CatalogoItem(d.getIdDepartamento(), d.getNombreDepartamento()))
                .toList();
    }

    public List<CatalogoItem> ciudades(Long idDepartamento) {
        return ciudadRepository.findByDepartamentoIdDepartamentoOrderByNombreCiudad(idDepartamento).stream()
                .map(c -> new CatalogoItem(c.getIdCiudad(), c.getNombreCiudad()))
                .toList();
    }

    /**
     * Países, departamentos y ciudades para registro, sedes y empleados, con la misma forma que
     * UBICACIONES del frontend: { "Colombia": { "Antioquia": ["Medellín", "Envigado"], ... }, ... }.
     * No incluye los países que solo tienen ciudades de rutas (Francia, Italia...).
     */
    public Map<String, Map<String, List<String>>> ubicaciones() {
        Set<Long> ciudadesConRutas = ciudadRepository.listarConRutas().stream()
                .map(Ciudad::getIdCiudad)
                .collect(Collectors.toSet());
        List<Ciudad> ciudades = ciudadRepository.listarConUbicacion();
        Set<String> paisesConUbicaciones = ciudades.stream()
                .filter(c -> !ciudadesConRutas.contains(c.getIdCiudad()))
                .map(c -> c.getDepartamento().getPais().getNombrePais())
                .collect(Collectors.toSet());

        Map<String, Map<String, List<String>>> ubicaciones = new LinkedHashMap<>();
        for (Ciudad ciudad : ciudades) {
            Departamento departamento = ciudad.getDepartamento();
            String pais = departamento.getPais().getNombrePais();
            if (paisesConUbicaciones.contains(pais)) {
                ubicaciones.computeIfAbsent(pais, p -> new LinkedHashMap<>())
                        .computeIfAbsent(departamento.getNombreDepartamento(), d -> new ArrayList<>())
                        .add(ciudad.getNombreCiudad());
            }
        }
        return ubicaciones;
    }

    /** Ciudades que tienen rutas: las que se usan en la búsqueda de viajes. */
    public List<CiudadResponse> ciudadesDestino() {
        return ciudadRepository.listarConRutas().stream()
                .map(CiudadResponse::from)
                .toList();
    }

    public List<CatalogoItem> roles() {
        return rolRepository.findAllByOrderByNombreRol().stream()
                .map(r -> new CatalogoItem(r.getIdRol(), r.getNombreRol()))
                .toList();
    }

    public List<CatalogoItem> tiposIdentificacion() {
        return tipoIdentificacionRepository.findAllByOrderByNombreIdentificacion().stream()
                .map(t -> new CatalogoItem(t.getIdTipoIdentificacion(), t.getNombreIdentificacion()))
                .toList();
    }

    public List<CatalogoItem> cargos() {
        return cargoRepository.findAllByOrderByNombreCargo().stream()
                .map(c -> new CatalogoItem(c.getIdCargo(), c.getNombreCargo()))
                .toList();
    }

    public List<CatalogoItem> estadosNotificacion() {
        return estadoNotificacionRepository.findAllByOrderByNombreEstadoNotificacion().stream()
                .map(e -> new CatalogoItem(e.getIdEstadoNotificacion(), e.getNombreEstadoNotificacion()))
                .toList();
    }

    public List<CatalogoItem> estadosReserva() {
        return estadoReservaRepository.findAllByOrderByNombreEstadoReserva().stream()
                .map(e -> new CatalogoItem(e.getIdEstadoReserva(), e.getNombreEstadoReserva()))
                .toList();
    }

    public List<CatalogoItem> estadosTransaccion() {
        return estadoTransaccionRepository.findAllByOrderByNombreEstadoTransaccion().stream()
                .map(e -> new CatalogoItem(e.getIdEstadoTransaccion(), e.getNombreEstadoTransaccion()))
                .toList();
    }

    public List<CatalogoItem> metodosPago() {
        return metodoPagoRepository.findAllByOrderByNombreMetodoPago().stream()
                .map(m -> new CatalogoItem(m.getIdMetodoPago(), m.getNombreMetodoPago()))
                .toList();
    }

    public List<CatalogoItem> tiposTransporte() {
        return tipoTransporteRepository.findAllByOrderByNombreTipoTransporte().stream()
                .map(t -> new CatalogoItem(t.getIdTipoTransporte(), t.getNombreTipoTransporte()))
                .toList();
    }

    public List<CatalogoItem> tiposTrayecto() {
        return tipoTrayectoRepository.findAllByOrderByNombreTipoTrayecto().stream()
                .map(t -> new CatalogoItem(t.getIdTipoTrayecto(), t.getNombreTipoTrayecto()))
                .toList();
    }

    // -------------------------------------------- búsqueda por nombre (JSON)

    public Rol rol(String nombre) {
        return encontrar(rolRepository.findByNombreRolIgnoreCase(limpiar(nombre)), "rol", nombre);
    }

    public TipoIdentificacion tipoIdentificacion(String nombre) {
        return encontrar(tipoIdentificacionRepository.findByNombreIdentificacionIgnoreCase(limpiar(nombre)),
                "tipo de identificación", nombre);
    }

    public EstadoNotificacion estadoNotificacion(String nombre) {
        return encontrar(estadoNotificacionRepository.findByNombreEstadoNotificacionIgnoreCase(limpiar(nombre)),
                "estado de notificación", nombre);
    }

    public EstadoReserva estadoReserva(String nombre) {
        return encontrar(estadoReservaRepository.findByNombreEstadoReservaIgnoreCase(limpiar(nombre)),
                "estado de reserva", nombre);
    }

    public EstadoTransaccion estadoTransaccion(String nombre) {
        return encontrar(estadoTransaccionRepository.findByNombreEstadoTransaccionIgnoreCase(limpiar(nombre)),
                "estado de transacción", nombre);
    }

    public MetodoPago metodoPago(String nombre) {
        return encontrar(metodoPagoRepository.findByNombreMetodoPagoIgnoreCase(limpiar(nombre)),
                "método de pago", nombre);
    }

    public TipoTransporte tipoTransporte(String nombre) {
        return encontrar(tipoTransporteRepository.findByNombreTipoTransporteIgnoreCase(limpiar(nombre)),
                "tipo de transporte", nombre);
    }

    public TipoTrayecto tipoTrayecto(String nombre) {
        return encontrar(tipoTrayectoRepository.findByNombreTipoTrayectoIgnoreCase(limpiar(nombre)),
                "tipo de trayecto", nombre);
    }

    /** Ciudad por nombres (como en el formulario): debe existir en ese departamento y país. */
    public Ciudad ciudad(String nombrePais, String nombreDepartamento, String nombreCiudad) {
        Pais pais = paisRepository.findByNombrePaisIgnoreCase(nombrePais.trim())
                .orElseThrow(() -> new SolicitudInvalidaException("No existe el país " + nombrePais));
        Departamento departamento = departamentoRepository
                .findByNombreDepartamentoIgnoreCaseAndPaisIdPais(nombreDepartamento.trim(), pais.getIdPais())
                .orElseThrow(() -> new SolicitudInvalidaException("El departamento " + nombreDepartamento
                        + " no existe en " + pais.getNombrePais()));
        return ciudadRepository
                .findByNombreCiudadIgnoreCaseAndDepartamentoIdDepartamento(nombreCiudad.trim(),
                        departamento.getIdDepartamento())
                .orElseThrow(() -> new SolicitudInvalidaException("La ciudad " + nombreCiudad
                        + " no existe en " + departamento.getNombreDepartamento()));
    }

    public Cargo cargo(Long idCargo) {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe el cargo con id " + idCargo));
    }

    private static String limpiar(String nombre) {
        return nombre == null ? "" : nombre.trim();
    }

    private static <T> T encontrar(Optional<T> valor, String catalogo, String nombre) {
        return valor.orElseThrow(() -> new SolicitudInvalidaException(
                "El " + catalogo + " '" + nombre + "' no existe. Consulta los valores en /api/catalogos"));
    }
}
