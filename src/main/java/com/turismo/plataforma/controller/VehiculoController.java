package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.vehiculo.VehiculoRequest;
import com.turismo.plataforma.dto.vehiculo.VehiculoResponse;
import com.turismo.plataforma.service.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService servicio;

    public VehiculoController(VehiculoService servicio) {
        this.servicio = servicio;
    }

    /** Filtro opcional ?q= */
    @GetMapping
    public List<VehiculoResponse> listar(@RequestParam(name = "q", required = false) String q) {
        return servicio.listar(q);
    }

    @GetMapping("/{id}")
    public VehiculoResponse obtener(@PathVariable("id") Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest request) {
        VehiculoResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/api/vehiculos/" + creado.idVehiculo())).body(creado);
    }

    @PutMapping("/{id}")
    public VehiculoResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody VehiculoRequest request) {
        return servicio.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
