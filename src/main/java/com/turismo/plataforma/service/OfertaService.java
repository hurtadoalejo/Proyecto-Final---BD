package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.oferta.OfertaRequest;
import com.turismo.plataforma.dto.oferta.OfertaResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.Oferta;
import com.turismo.plataforma.repository.OfertaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/** Ofertas: porcentaje de descuento que se aplica a una reserva mientras esté vigente. */
@Service
@Transactional(readOnly = true)
public class OfertaService {

    private final OfertaRepository ofertaRepository;

    public OfertaService(OfertaRepository ofertaRepository) {
        this.ofertaRepository = ofertaRepository;
    }

    /** vigentes = true: solo las que aplican hoy. */
    public List<OfertaResponse> listar(boolean soloVigentes) {
        LocalDate hoy = LocalDate.now();
        List<Oferta> ofertas = soloVigentes
                ? ofertaRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaFin(hoy, hoy)
                : ofertaRepository.findAllByOrderByFechaInicioDesc();
        return ofertas.stream().map(OfertaResponse::from).toList();
    }

    public OfertaResponse obtener(Long id) {
        return OfertaResponse.from(buscarEntidad(id));
    }

    @Transactional
    public OfertaResponse crear(OfertaRequest request) {
        validarFechas(request);
        Oferta oferta = new Oferta();
        aplicar(oferta, request);
        return OfertaResponse.from(ofertaRepository.save(oferta));
    }

    @Transactional
    public OfertaResponse actualizar(Long id, OfertaRequest request) {
        Oferta oferta = buscarEntidad(id);
        validarFechas(request);
        aplicar(oferta, request);
        return OfertaResponse.from(oferta);
    }

    /** Falla con 409 si alguna reserva o notificación usa la oferta. */
    @Transactional
    public void eliminar(Long id) {
        ofertaRepository.delete(buscarEntidad(id));
        ofertaRepository.flush();
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
