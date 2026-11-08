package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.ruta.RutaRequest;
import com.turismo.plataforma.dto.ruta.RutaResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.repository.RutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    public List<RutaResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return rutaRepository
                .findByDireccionOrigenContainingIgnoreCaseOrDireccionDestinoContainingIgnoreCaseOrderByDireccionOrigen(
                        filtro, filtro)
                .stream()
                .map(RutaResponse::from)
                .toList();
    }

    /** Consulta: una ruta por su id. */
    public RutaResponse obtener(Long id) {
        return RutaResponse.from(buscarEntidad(id));
    }

    @Transactional
    public RutaResponse crear(RutaRequest request) {
        validarOrigenDestino(request);
        Ruta ruta = new Ruta();
        aplicarDatos(ruta, request);
        return RutaResponse.from(rutaRepository.save(ruta));
    }

    @Transactional
    public RutaResponse actualizar(Long id, RutaRequest request) {
        Ruta ruta = buscarEntidad(id);
        validarOrigenDestino(request);
        aplicarDatos(ruta, request);
        return RutaResponse.from(ruta);
    }

    @Transactional
    public void eliminar(Long id) {
        Ruta ruta = buscarEntidad(id);
        rutaRepository.delete(ruta);
        rutaRepository.flush();
    }

    private Ruta buscarEntidad(Long id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la ruta con id " + id));
    }

    private void validarOrigenDestino(RutaRequest request) {
        if (request.direccionOrigen().trim().equalsIgnoreCase(request.direccionDestino().trim())) {
            throw new ConflictoException("El origen y el destino no pueden ser iguales");
        }
    }

    private void aplicarDatos(Ruta ruta, RutaRequest request) {
        ruta.setDireccionOrigen(request.direccionOrigen().trim());
        ruta.setDireccionDestino(request.direccionDestino().trim());
        ruta.setDistancia(request.distancia());
        ruta.setDuracionEstimada(request.duracionEstimada());
        ruta.setPrecioBase(request.precioBase());
    }
}