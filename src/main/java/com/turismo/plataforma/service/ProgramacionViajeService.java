package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.programacion.ProgramacionViajeRequest;
import com.turismo.plataforma.dto.programacion.ProgramacionViajeResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.ProgramacionViaje;
import com.turismo.plataforma.model.Ruta;
import com.turismo.plataforma.model.Vehiculo;
import com.turismo.plataforma.repository.ProgramacionViajeRepository;
import com.turismo.plataforma.repository.RutaRepository;
import com.turismo.plataforma.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProgramacionViajeService {

    private final ProgramacionViajeRepository programacionRepository;
    private final RutaRepository rutaRepository;
    private final VehiculoRepository vehiculoRepository;

    public ProgramacionViajeService(ProgramacionViajeRepository programacionRepository,
                                    RutaRepository rutaRepository,
                                    VehiculoRepository vehiculoRepository) {
        this.programacionRepository = programacionRepository;
        this.rutaRepository = rutaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<ProgramacionViajeResponse> listar(Long idRuta) {
        List<ProgramacionViaje> programaciones;
        if (idRuta == null) {
            programaciones = programacionRepository.listarConRutaYVehiculo();
        } else {
            buscarRuta(idRuta);
            programaciones = programacionRepository.listarPorRuta(idRuta);
        }
        return programaciones.stream()
                .map(ProgramacionViajeResponse::from)
                .toList();
    }

    public ProgramacionViajeResponse obtener(Long id) {
        return ProgramacionViajeResponse.from(buscarEntidad(id));
    }

    @Transactional
    public ProgramacionViajeResponse crear(ProgramacionViajeRequest request) {
        ProgramacionViaje programacion = new ProgramacionViaje();
        aplicarDatos(programacion, request);
        if (programacionRepository.existsByVehiculoIdVehiculoAndFechaHoraSalida(
                request.idVehiculo(), request.fechaHoraSalida())) {
            throw new ConflictoException("El vehículo ya tiene un viaje programado en esa fecha y hora");
        }
        return ProgramacionViajeResponse.from(programacionRepository.save(programacion));
    }

    @Transactional
    public ProgramacionViajeResponse actualizar(Long id, ProgramacionViajeRequest request) {
        ProgramacionViaje programacion = buscarEntidad(id);
        if (programacionRepository.existsByVehiculoIdVehiculoAndFechaHoraSalidaAndIdProgramacionNot(
                request.idVehiculo(), request.fechaHoraSalida(), id)) {
            throw new ConflictoException("El vehículo ya tiene otro viaje programado en esa fecha y hora");
        }
        aplicarDatos(programacion, request);
        return ProgramacionViajeResponse.from(programacion);
    }

    @Transactional
    public void eliminar(Long id) {
        ProgramacionViaje programacion = buscarEntidad(id);
        programacionRepository.delete(programacion);
        programacionRepository.flush();
    }

    private ProgramacionViaje buscarEntidad(Long id) {
        return programacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la programación con id " + id));
    }

    private void aplicarDatos(ProgramacionViaje programacion, ProgramacionViajeRequest request) {
        programacion.setRuta(buscarRuta(request.idRuta()));
        programacion.setVehiculo(buscarVehiculo(request.idVehiculo()));
        programacion.setFechaHoraSalida(request.fechaHoraSalida());
    }

    private Ruta buscarRuta(Long idRuta) {
        return rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la ruta con id " + idRuta));
    }

    private Vehiculo buscarVehiculo(Long idVehiculo) {
        return vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo con id " + idVehiculo));
    }
}