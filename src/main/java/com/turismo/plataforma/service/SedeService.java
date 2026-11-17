package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.sede.SedeRequest;
import com.turismo.plataforma.dto.sede.SedeResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Sede;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.repository.EmpleadoRepository;
import com.turismo.plataforma.repository.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SedeService {

    private final SedeRepository sedeRepository;
    private final EmpleadoRepository empleadoRepository;
    private final CatalogoService catalogoService;

    public SedeService(SedeRepository sedeRepository, EmpleadoRepository empleadoRepository,
                       CatalogoService catalogoService) {
        this.sedeRepository = sedeRepository;
        this.empleadoRepository = empleadoRepository;
        this.catalogoService = catalogoService;
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
        aplicar(sede, request, catalogoService.ciudad(request.pais(), request.departamento(), request.ciudad()));
        return SedeResponse.from(sedeRepository.save(sede));
    }

    @Transactional
    public SedeResponse actualizar(Long id, SedeRequest request) {
        Sede sede = buscarEntidad(id);
        String nombre = request.nombreSede().trim();
        if (sedeRepository.existsByNombreSedeIgnoreCaseAndIdSedeNot(nombre, id)) {
            throw new ConflictoException("Ya existe una sede llamada " + nombre);
        }
        aplicar(sede, request, catalogoService.ciudad(request.pais(), request.departamento(), request.ciudad()));
        return SedeResponse.from(sede);
    }

    /** No deja eliminar una sede que tiene empleados trabajando en ella (409 con el motivo). */
    @Transactional
    public void eliminar(Long id) {
        Sede sede = buscarEntidad(id);
        long empleados = empleadoRepository.countBySedeIdSede(id);
        if (empleados > 0) {
            throw new ConflictoException("No se puede eliminar la sede " + sede.getNombreSede() + ": tiene "
                    + empleados + (empleados == 1 ? " empleado trabajando" : " empleados trabajando")
                    + " en ella. Reasígnalos a otra sede primero");
        }
        sedeRepository.delete(sede);
        sedeRepository.flush();
    }

    private static void aplicar(Sede sede, SedeRequest request, Ciudad ciudad) {
        sede.setNombreSede(request.nombreSede().trim());
        sede.setDireccion(request.direccion().trim());
        sede.setCiudad(ciudad);
    }

    private Sede buscarEntidad(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la sede con id " + id));
    }
}
