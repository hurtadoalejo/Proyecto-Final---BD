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

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoResponse> listar() {
        return vehiculoService.listar();
    }

    @GetMapping("/{id}")
    public VehiculoResponse obtener(@PathVariable("id") Long id) {
        return vehiculoService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest request) {
        VehiculoResponse creado = vehiculoService.crear(request);
        return ResponseEntity.created(URI.create("/api/vehiculos/" + creado.idVehiculo())).body(creado);
    }

    @PutMapping("/{id}")
    public VehiculoResponse actualizar(@PathVariable("id") Long id,
                                       @Valid @RequestBody VehiculoRequest request) {
        return vehiculoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}