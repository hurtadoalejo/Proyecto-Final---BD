package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.sede.SedeRequest;
import com.turismo.plataforma.dto.sede.SedeResponse;
import com.turismo.plataforma.service.SedeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    private final SedeService servicio;

    public SedeController(SedeService servicio) {
        this.servicio = servicio;
    }

    /** Filtro opcional ?q= */
    @GetMapping
    public List<SedeResponse> listar(@RequestParam(name = "q", required = false) String q) {
        return servicio.listar(q);
    }

    @GetMapping("/{id}")
    public SedeResponse obtener(@PathVariable("id") Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<SedeResponse> crear(@Valid @RequestBody SedeRequest request) {
        SedeResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/api/sedes/" + creado.idSede())).body(creado);
    }

    @PutMapping("/{id}")
    public SedeResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody SedeRequest request) {
        return servicio.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
