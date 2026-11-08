package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.sede.SedeRequest;
import com.turismo.plataforma.dto.sede.SedeResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Sede;
import com.turismo.plataforma.repository.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SedeService {

    private final SedeRepository sedeRepository;

    public SedeService(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    public List<SedeResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return sedeRepository.findByNombreSedeContainingIgnoreCaseOrderByNombreSede(filtro).stream()
                .map(SedeResponse::from)
                .toList();
    }

    /** Consulta: una sede por su id. */
    public SedeResponse obtener(Long id) {
        return SedeResponse.from(buscarEntidad(id));
    }

    @Transactional
    public SedeResponse crear(SedeRequest request) {
        String nombre = request.nombreSede().trim();
        if (sedeRepository.existsByNombreSedeIgnoreCase(nombre)) {
            throw new ConflictoException("Ya existe una sede llamada " + nombre);
        }
        Sede sede = new Sede();
        aplicarDatos(sede, request);
        return SedeResponse.from(sedeRepository.save(sede));
    }

    @Transactional
    public SedeResponse actualizar(Long id, SedeRequest request) {
        Sede sede = buscarEntidad(id);
        String nombre = request.nombreSede().trim();
        if (sedeRepository.existsByNombreSedeIgnoreCaseAndIdSedeNot(nombre, id)) {
            throw new ConflictoException("Ya existe otra sede llamada " + nombre);
        }
        aplicarDatos(sede, request);
        return SedeResponse.from(sede);
    }

    @Transactional
    public void eliminar(Long id) {
        Sede sede = buscarEntidad(id);
        sedeRepository.delete(sede);
        sedeRepository.flush();
    }

    private Sede buscarEntidad(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la sede con id " + id));
    }

    private void aplicarDatos(Sede sede, SedeRequest request) {
        sede.setNombreSede(request.nombreSede().trim());
        sede.setDireccion(request.direccion().trim());
    }
}