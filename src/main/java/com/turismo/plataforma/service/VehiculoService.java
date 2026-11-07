package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.vehiculo.VehiculoResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Vehiculo;
import com.turismo.plataforma.repository.VehiculoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    /** Consulta: todos los vehículos ordenados */
    public List<VehiculoResponse> listar() {
        return vehiculoRepository.findAll(Sort.by("codigoIdentificacion")).stream()
                .map(VehiculoResponse::from)
                .toList();
    }

    /** Consulta: un vehículo por su id */
    public VehiculoResponse obtener(Long id) {
        return VehiculoResponse.from(buscarEntidad(id));
    }

    private Vehiculo buscarEntidad(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo con id " + id));
    }
}