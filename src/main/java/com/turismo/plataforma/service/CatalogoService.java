package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.catalogo.CatalogoItem;
import com.turismo.plataforma.dto.catalogo.CiudadResponse;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.catalogo.*;
import com.turismo.plataforma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    /** Ciudades con código (BOG, MAD...): las que se usan en la búsqueda de viajes. */
    public List<CiudadResponse> ciudadesDestino() {
        return ciudadRepository.listarConCodigo().stream()
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
