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

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public List<EmpleadoResponse> listar(@RequestParam(name = "idSede", required = false) Long idSede) {
        return empleadoService.listar(idSede);
    }

    @GetMapping("/{id}")
    public EmpleadoResponse obtener(@PathVariable("id") Long id) {
        return empleadoService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoRequest request) {
        EmpleadoResponse creado = empleadoService.crear(request);
        return ResponseEntity.created(URI.create("/api/empleados/" + creado.idUsuario())).body(creado);
    }

    @PutMapping("/{id}")
    public EmpleadoResponse actualizar(@PathVariable("id") Long id,
                                       @Valid @RequestBody EmpleadoActualizarRequest request) {
        return empleadoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}