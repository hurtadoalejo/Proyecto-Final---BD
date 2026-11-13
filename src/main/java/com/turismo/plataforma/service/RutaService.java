package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.ruta.*;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.ProgramacionViaje;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.model.Vehiculo;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.repository.CiudadRepository;
import com.turismo.plataforma.repository.ProgramacionViajeRepository;
import com.turismo.plataforma.repository.RutaRepository;
import com.turismo.plataforma.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Rutas (origen -> destino con su vehículo) y la búsqueda de viajes del frontend. */
@Service
@Transactional(readOnly = true)
public class RutaService {

    private final RutaRepository rutaRepository;
    private final CiudadRepository ciudadRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ProgramacionViajeRepository programacionViajeRepository;
    private final CatalogoService catalogoService;

    public RutaService(RutaRepository rutaRepository, CiudadRepository ciudadRepository,
                       VehiculoRepository vehiculoRepository,
                       ProgramacionViajeRepository programacionViajeRepository,
                       CatalogoService catalogoService) {
        this.rutaRepository = rutaRepository;
        this.ciudadRepository = ciudadRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.programacionViajeRepository = programacionViajeRepository;
        this.catalogoService = catalogoService;
    }

    /** Rutas cuyo origen o destino coincide con el texto (nombre o código); todas si viene vacío. */
    public List<RutaResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return rutaRepository.buscar(filtro).stream()
                .map(RutaResponse::from)
                .toList();
    }

    public RutaResponse obtener(Long id) {
        return RutaResponse.from(buscarEntidad(id));
    }

    @Transactional
    public RutaResponse crear(RutaRequest request) {
        Datos datos = validar(request);
        Ruta ruta = new Ruta();
        aplicar(ruta, request, datos);
        return RutaResponse.from(rutaRepository.save(ruta));
    }

    @Transactional
    public RutaResponse actualizar(Long id, RutaRequest request) {
        Ruta ruta = buscarEntidad(id);
        Datos datos = validar(request);
        aplicar(ruta, request, datos);
        return RutaResponse.from(ruta);
    }

    /** Falla con 409 si la ruta tiene salidas programadas. */
    @Transactional
    public void eliminar(Long id) {
        rutaRepository.delete(buscarEntidad(id));
        rutaRepository.flush();
    }

    /**
     * Búsqueda del frontend. Por cada tramo devuelve las salidas de ese día entre las dos ciudades,
     * en el tipo de transporte pedido y con asientos suficientes para todos los pasajeros.
     */
    public BusquedaRutasResponse buscar(BusquedaRutasRequest request) {
        String tipoTransporte = catalogoService.tipoTransporte(request.tipoTransporte()).getNombreTipoTransporte();
        int pasajeros = request.pasajeros();

        List<TramoResultado> resultados = new ArrayList<>();
        LocalDate fechaAnterior = null;
        for (TramoRequest tramo : request.tramos()) {
            if (fechaAnterior != null && tramo.fecha().isBefore(fechaAnterior)) {
                throw new SolicitudInvalidaException("Las fechas de los tramos deben ir en orden");
            }
            fechaAnterior = tramo.fecha();
            resultados.add(buscarTramo(tramo, tipoTransporte, pasajeros));
        }
        return new BusquedaRutasResponse(tipoTransporte, pasajeros, resultados);
    }

    private TramoResultado buscarTramo(TramoRequest tramo, String tipoTransporte, int pasajeros) {
        Ciudad origen = buscarPorCodigo(tramo.origen());
        Ciudad destino = buscarPorCodigo(tramo.destino());
        if (origen.getIdCiudad().equals(destino.getIdCiudad())) {
            throw new SolicitudInvalidaException("El origen y el destino de un tramo deben ser diferentes");
        }

        // Si la fecha es hoy, solo salidas que aún no han partido.
        LocalDateTime inicioDia = tramo.fecha().atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime desde = inicioDia.isBefore(ahora) ? ahora : inicioDia;
        LocalDateTime hasta = tramo.fecha().plusDays(1).atStartOfDay();

        List<ViajeDisponible> viajes = new ArrayList<>();
        for (ProgramacionViaje salida : programacionViajeRepository.buscarSalidas(
                origen.getCodigoCiudad(), destino.getCodigoCiudad(), tipoTransporte, desde, hasta)) {
            long disponibles = salida.getRuta().getVehiculo().getCapacidad()
                    - programacionViajeRepository.contarAsientosOcupados(salida.getIdProgramacionViaje());
            if (disponibles >= pasajeros) {
                viajes.add(aViajeDisponible(salida, pasajeros, disponibles));
            }
        }
        return new TramoResultado(origen.getCodigoCiudad(), origen.getNombreCiudad(),
                destino.getCodigoCiudad(), destino.getNombreCiudad(), tramo.fecha(), viajes);
    }

    private static ViajeDisponible aViajeDisponible(ProgramacionViaje salida, int pasajeros, long disponibles) {
        Ruta ruta = salida.getRuta();
        Vehiculo vehiculo = ruta.getVehiculo();
        return new ViajeDisponible(
                salida.getIdProgramacionViaje(),
                ruta.getIdRuta(),
                salida.getFechaHoraSalida(),
                salida.calcularLlegada(),
                ruta.getDuracionEstimada(),
                ruta.getDireccionOrigen(),
                ruta.getDireccionDestino(),
                vehiculo.getTipoTransporte().getNombreTipoTransporte(),
                vehiculo.getCodigoIdentificacion(),
                ruta.getDistancia(),
                ruta.getPrecioBase(),
                ruta.getPrecioBase().multiply(BigDecimal.valueOf(pasajeros)),
                disponibles);
    }

    private Ciudad buscarPorCodigo(String codigo) {
        String limpio = codigo.trim().toUpperCase(Locale.ROOT);
        return ciudadRepository.findByCodigoCiudadIgnoreCase(limpio)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe una ciudad con el código " + limpio
                        + ". Consulta GET /api/catalogos/ciudades-destino"));
    }

    /** Lo que se consulta antes de modificar la ruta. */
    private record Datos(Ciudad origen, Ciudad destino, Vehiculo vehiculo) {
    }

    private Datos validar(RutaRequest request) {
        if (request.idCiudadOrigen().equals(request.idCiudadDestino())) {
            throw new SolicitudInvalidaException("El origen y el destino deben ser diferentes");
        }
        Ciudad origen = buscarCiudad(request.idCiudadOrigen());
        Ciudad destino = buscarCiudad(request.idCiudadDestino());
        Vehiculo vehiculo = vehiculoRepository.findById(request.idVehiculo())
                .orElseThrow(() -> new SolicitudInvalidaException(
                        "No existe el vehículo con id " + request.idVehiculo()));
        return new Datos(origen, destino, vehiculo);
    }

    private static void aplicar(Ruta ruta, RutaRequest request, Datos datos) {
        ruta.setCiudadOrigen(datos.origen());
        ruta.setDireccionOrigen(opcional(request.direccionOrigen()));
        ruta.setCiudadDestino(datos.destino());
        ruta.setDireccionDestino(opcional(request.direccionDestino()));
        ruta.setDistancia(request.distancia());
        ruta.setDuracionEstimada(request.duracionEstimada());
        ruta.setPrecioBase(request.precioBase());
        ruta.asignarVehiculo(datos.vehiculo());
    }

    private static String opcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private Ciudad buscarCiudad(Long idCiudad) {
        return ciudadRepository.findById(idCiudad)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la ciudad con id " + idCiudad));
    }

    private Ruta buscarEntidad(Long id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la ruta con id " + id));
    }
}
