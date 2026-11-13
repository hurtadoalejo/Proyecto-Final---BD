package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.empleado.EmpleadoActualizarRequest;
import com.turismo.plataforma.dto.empleado.EmpleadoRequest;
import com.turismo.plataforma.dto.empleado.EmpleadoResponse;
import com.turismo.plataforma.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Empleados (rol EMPLEADO o ADMIN). La contraseña solo se envía al crear. */
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService servicio;

    public EmpleadoController(EmpleadoService servicio) {
        this.servicio = servicio;
    }

    /** ?idSede= opcional: empleados de una sede. */
    @GetMapping
    public List<EmpleadoResponse> listar(@RequestParam(name = "idSede", required = false) Long idSede) {
        return servicio.listar(idSede);
    }

    @GetMapping("/{id}")
    public EmpleadoResponse obtener(@PathVariable("id") Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoRequest request) {
        EmpleadoResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/api/empleados/" + creado.datosPersonales().idUsuario())).body(creado);
    }

    @PutMapping("/{id}")
    public EmpleadoResponse actualizar(@PathVariable("id") Long id,
                                       @Valid @RequestBody EmpleadoActualizarRequest request) {
        return servicio.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
