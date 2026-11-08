package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.ruta.RutaResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.repository.RutaRepository;
import org.springframework.data.domain.Sort;
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

    public List<RutaResponse> listar() {
        return rutaRepository.findAll(Sort.by("direccionOrigen")).stream()
                .map(RutaResponse::from)
                .toList();
    }

    public RutaResponse obtener(Long id) {
        return RutaResponse.from(buscarEntidad(id));
    }

    private Ruta buscarEntidad(Long id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la ruta con id " + id));
    }
}