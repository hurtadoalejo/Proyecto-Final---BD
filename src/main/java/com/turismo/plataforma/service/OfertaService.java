package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.oferta.CuponResponse;
import com.turismo.plataforma.dto.oferta.OfertaRequest;
import com.turismo.plataforma.dto.oferta.OfertaResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.Notificacion;
import com.turismo.plataforma.model.Oferta;
import com.turismo.plataforma.model.catalogo.EstadoNotificacion;
import com.turismo.plataforma.repository.ClienteRepository;
import com.turismo.plataforma.repository.NotificacionRepository;
import com.turismo.plataforma.repository.OfertaRepository;
import com.turismo.plataforma.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Ofertas: porcentaje de descuento que se aplica a una reserva mientras esté vigente. */
@Service
@Transactional(readOnly = true)
public class OfertaService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final OfertaRepository ofertaRepository;
    private final NotificacionRepository notificacionRepository;
    private final ClienteRepository clienteRepository;
    private final CatalogoService catalogoService;
    private final ReservaRepository reservaRepository;

    public OfertaService(OfertaRepository ofertaRepository, NotificacionRepository notificacionRepository,
                         ClienteRepository clienteRepository, CatalogoService catalogoService,
                         ReservaRepository reservaRepository) {
        this.ofertaRepository = ofertaRepository;
        this.notificacionRepository = notificacionRepository;
        this.clienteRepository = clienteRepository;
        this.catalogoService = catalogoService;
        this.reservaRepository = reservaRepository;
    }

    /** vigentes = true: solo las que aplican hoy. */
    public List<OfertaResponse> listar(boolean soloVigentes) {
        LocalDate hoy = LocalDate.now();
        List<Oferta> ofertas = soloVigentes
                ? ofertaRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaFin(hoy, hoy)
                : ofertaRepository.findAllByOrderByFechaInicioDesc();
        return ofertas.stream().map(OfertaResponse::from).toList();
    }

    /**
     * Ofertas disponibles para un cliente: las vigentes hoy que todavía no ha usado en una reserva
     * (cada oferta se usa una sola vez; si cancela la reserva, la oferta vuelve a quedar disponible).
     * fechaNotificacion es la primera vez que se le avisó de la oferta (null si no le llegó aviso).
     */
    public List<CuponResponse> cuponesDeCliente(Long idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new RecursoNoEncontradoException("No existe el cliente con id " + idCliente);
        }
        LocalDate hoy = LocalDate.now();
        Map<Long, LocalDateTime> avisos = new LinkedHashMap<>();
        for (Notificacion n : notificacionRepository.listarPorCliente(idCliente)) {
            if (n.getOferta() != null && n.getFechaEnvio() != null) {
                avisos.merge(n.getOferta().getIdOferta(), n.getFechaEnvio(),
                        (x, y) -> x.isBefore(y) ? x : y);
            }
        }
        Set<Long> usadas = new HashSet<>(reservaRepository.ofertasUsadasPorCliente(idCliente));
        return ofertaRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaFin(hoy, hoy)
                .stream()
                .filter(oferta -> !usadas.contains(oferta.getIdOferta()))
                .map(oferta -> new CuponResponse(oferta.getIdOferta(), oferta.getTitulo(), oferta.getDescripcion(),
                        oferta.getPorcentajeDescuento(), oferta.getFechaInicio(), oferta.getFechaFin(),
                        avisos.get(oferta.getIdOferta())))
                .sorted(Comparator.comparing(CuponResponse::fechaFin))
                .toList();
    }

    public OfertaResponse obtener(Long id) {
        return OfertaResponse.from(buscarEntidad(id));
    }

    /** Crea la oferta y la notifica automáticamente a todos los clientes (simulado: queda registrado en la BD). */
    @Transactional
    public OfertaResponse crear(OfertaRequest request) {
        validarFechas(request);
        Oferta oferta = new Oferta();
        aplicar(oferta, request);
        Oferta guardada = ofertaRepository.save(oferta);
        notificarClientes(guardada);
        return OfertaResponse.from(guardada);
    }

    @Transactional
    public OfertaResponse actualizar(Long id, OfertaRequest request) {
        Oferta oferta = buscarEntidad(id);
        validarFechas(request);
        aplicar(oferta, request);
        return OfertaResponse.from(oferta);
    }

    /** Borra también sus notificaciones. Falla con 409 si alguna reserva usa la oferta. */
    @Transactional
    public void eliminar(Long id) {
        Oferta oferta = buscarEntidad(id);
        notificacionRepository.deleteAll(notificacionRepository.findByOfertaIdOferta(id));
        ofertaRepository.delete(oferta);
        ofertaRepository.flush();
    }

    /** Una notificación ENVIADA, ligada a todos los clientes por la tabla ClienteNotificacion. */
    private void notificarClientes(Oferta oferta) {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            return;
        }
        String mensaje = "¡Nueva oferta! " + oferta.getTitulo() + ": "
                + oferta.getPorcentajeDescuento().stripTrailingZeros().toPlainString() + "% de descuento del "
                + oferta.getFechaInicio().format(FECHA) + " al " + oferta.getFechaFin().format(FECHA) + ".";
        if (oferta.getDescripcion() != null) {
            mensaje += " " + oferta.getDescripcion();
        }
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje.length() > 200 ? mensaje.substring(0, 197) + "..." : mensaje);
        notificacion.setEstadoNotificacion(catalogoService.estadoNotificacion(EstadoNotificacion.ENVIADO));
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setOferta(oferta);
        notificacion.getClientes().addAll(clientes);
        notificacionRepository.save(notificacion);
    }

    private static void validarFechas(OfertaRequest request) {
        if (request.fechaFin().isBefore(request.fechaInicio())) {
            throw new SolicitudInvalidaException("La fecha de fin no puede ser anterior a la de inicio");
        }
    }

    private static void aplicar(Oferta oferta, OfertaRequest request) {
        oferta.setTitulo(request.titulo().trim());
        oferta.setDescripcion(request.descripcion() == null || request.descripcion().isBlank()
                ? null : request.descripcion().trim());
        oferta.setPorcentajeDescuento(request.porcentajeDescuento());
        oferta.setFechaInicio(request.fechaInicio());
        oferta.setFechaFin(request.fechaFin());
    }

    private Oferta buscarEntidad(Long id) {
        return ofertaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la oferta con id " + id));
    }
}
