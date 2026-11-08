package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.vehiculo.VehiculoRequest;
import com.turismo.plataforma.dto.vehiculo.VehiculoResponse;
import com.turismo.plataforma.exception.ConflictoException;
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

    /** Consulta: vehículos cuyo código contiene el texto (todos si viene vacío). */
    public List<VehiculoResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return vehiculoRepository
                .findByCodigoIdentificacionContainingIgnoreCaseOrderByCodigoIdentificacion(filtro)
                .stream()
                .map(VehiculoResponse::from)
                .toList();
    }

    /** vehiculo por su id */
    public VehiculoResponse obtener(Long id) {
        return VehiculoResponse.from(buscarEntidad(id));
    }

    @Transactional
    public VehiculoResponse crear(VehiculoRequest request) {
        String codigo = normalizarCodigo(request.codigoIdentificacion());
        if (vehiculoRepository.existsByCodigoIdentificacion(codigo)) {
            throw new ConflictoException("Ya existe un vehículo con el código " + codigo);
        }
        Vehiculo vehiculo = new Vehiculo();
        aplicarDatos(vehiculo, request, codigo);
        return VehiculoResponse.from(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public VehiculoResponse actualizar(Long id, VehiculoRequest request) {
        Vehiculo vehiculo = buscarEntidad(id);
        String codigo = normalizarCodigo(request.codigoIdentificacion());
        if (vehiculoRepository.existsByCodigoIdentificacionAndIdVehiculoNot(codigo, id)) {
            throw new ConflictoException("Ya existe otro vehículo con el código " + codigo);
        }
        aplicarDatos(vehiculo, request, codigo);
        return VehiculoResponse.from(vehiculo);
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = buscarEntidad(id);
        vehiculoRepository.delete(vehiculo);
        vehiculoRepository.flush();
    }

    private Vehiculo buscarEntidad(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo con id " + id));
    }

    private void aplicarDatos(Vehiculo vehiculo, VehiculoRequest request, String codigo) {
        vehiculo.setCodigoIdentificacion(codigo);
        vehiculo.setTipoTransporte(request.tipoTransporte());
        vehiculo.setCapacidad(request.capacidad());
    }

    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase();
    }
}