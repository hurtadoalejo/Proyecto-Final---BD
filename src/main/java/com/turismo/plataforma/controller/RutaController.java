package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.ruta.RutaRequest;
import com.turismo.plataforma.dto.ruta.RutaResponse;
import com.turismo.plataforma.service.RutaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    public List<RutaResponse> listar(@RequestParam(name = "q", required = false) String q) {
        return rutaService.listar(q);
    }

    @GetMapping("/{id}")
    public RutaResponse obtener(@PathVariable("id") Long id) {
        return rutaService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<RutaResponse> crear(@Valid @RequestBody RutaRequest request) {
        RutaResponse creada = rutaService.crear(request);
        return ResponseEntity.created(URI.create("/api/rutas/" + creada.idRuta())).body(creada);
    }

    @PutMapping("/{id}")
    public RutaResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody RutaRequest request) {
        return rutaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        rutaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}