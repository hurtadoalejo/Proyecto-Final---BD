package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.vehiculo.VehiculoResponse;
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

    public List<VehiculoResponse> listar() {
        return vehiculoRepository.findAll(Sort.by("codigoIdentificacion")).stream()
                .map(VehiculoResponse::from)
                .toList();
    }
}