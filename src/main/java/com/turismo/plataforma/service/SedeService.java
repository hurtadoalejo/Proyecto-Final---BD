package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.sede.SedeRequest;
import com.turismo.plataforma.dto.sede.SedeResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.model.Sede;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.repository.CiudadRepository;
import com.turismo.plataforma.repository.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SedeService {

    private final SedeRepository sedeRepository;
    private final CiudadRepository ciudadRepository;

    public SedeService(SedeRepository sedeRepository, CiudadRepository ciudadRepository) {
        this.sedeRepository = sedeRepository;
        this.ciudadRepository = ciudadRepository;
    }

    public List<SedeResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return sedeRepository.buscar(filtro).stream()
                .map(SedeResponse::from)
                .toList();
    }

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
        aplicar(sede, request, buscarCiudad(request.idCiudad()));
        return SedeResponse.from(sedeRepository.save(sede));
    }

    @Transactional
    public SedeResponse actualizar(Long id, SedeRequest request) {
        Sede sede = buscarEntidad(id);
        String nombre = request.nombreSede().trim();
        if (sedeRepository.existsByNombreSedeIgnoreCaseAndIdSedeNot(nombre, id)) {
            throw new ConflictoException("Ya existe una sede llamada " + nombre);
        }
        aplicar(sede, request, buscarCiudad(request.idCiudad()));
        return SedeResponse.from(sede);
    }

    /** Falla con 409 si la sede tiene empleados. */
    @Transactional
    public void eliminar(Long id) {
        sedeRepository.delete(buscarEntidad(id));
        sedeRepository.flush();
    }

    private static void aplicar(Sede sede, SedeRequest request, Ciudad ciudad) {
        sede.setNombreSede(request.nombreSede().trim());
        sede.setDireccion(request.direccion().trim());
        sede.setCiudad(ciudad);
    }

    private Ciudad buscarCiudad(Long idCiudad) {
        return ciudadRepository.findById(idCiudad)
                .orElseThrow(() -> new SolicitudInvalidaException("No existe la ciudad con id " + idCiudad));
    }

    private Sede buscarEntidad(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la sede con id " + id));
    }
}
