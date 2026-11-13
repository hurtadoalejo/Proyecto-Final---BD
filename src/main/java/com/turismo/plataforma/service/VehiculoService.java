package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.vehiculo.VehiculoRequest;
import com.turismo.plataforma.dto.vehiculo.VehiculoResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Vehiculo;
import com.turismo.plataforma.model.catalogo.TipoTransporte;
import com.turismo.plataforma.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final CatalogoService catalogoService;

    public VehiculoService(VehiculoRepository vehiculoRepository, CatalogoService catalogoService) {
        this.vehiculoRepository = vehiculoRepository;
        this.catalogoService = catalogoService;
    }

    public List<VehiculoResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return vehiculoRepository.buscar(filtro).stream()
                .map(VehiculoResponse::from)
                .toList();
    }

    public VehiculoResponse obtener(Long id) {
        return VehiculoResponse.from(buscarEntidad(id));
    }

    @Transactional
    public VehiculoResponse crear(VehiculoRequest request) {
        String codigo = normalizarCodigo(request.codigoIdentificacion());
        if (vehiculoRepository.existsByCodigoIdentificacionIgnoreCase(codigo)) {
            throw new ConflictoException("Ya existe un vehículo con el código " + codigo);
        }
        TipoTransporte tipo = catalogoService.tipoTransporte(request.tipoTransporte());
        Vehiculo vehiculo = new Vehiculo();
        aplicar(vehiculo, codigo, tipo, request.capacidad());
        return VehiculoResponse.from(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public VehiculoResponse actualizar(Long id, VehiculoRequest request) {
        Vehiculo vehiculo = buscarEntidad(id);
        String codigo = normalizarCodigo(request.codigoIdentificacion());
        if (vehiculoRepository.existsByCodigoIdentificacionIgnoreCaseAndIdVehiculoNot(codigo, id)) {
            throw new ConflictoException("Ya existe un vehículo con el código " + codigo);
        }
        TipoTransporte tipo = catalogoService.tipoTransporte(request.tipoTransporte());
        aplicar(vehiculo, codigo, tipo, request.capacidad());
        return VehiculoResponse.from(vehiculo);
    }

    /** Falla con 409 si el vehículo cubre alguna ruta. */
    @Transactional
    public void eliminar(Long id) {
        vehiculoRepository.delete(buscarEntidad(id));
        vehiculoRepository.flush();
    }

    private static void aplicar(Vehiculo vehiculo, String codigo, TipoTransporte tipo, Integer capacidad) {
        vehiculo.setCodigoIdentificacion(codigo);
        vehiculo.setTipoTransporte(tipo);
        vehiculo.setCapacidad(capacidad);
    }

    private static String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }

    private Vehiculo buscarEntidad(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo con id " + id));
    }
}
