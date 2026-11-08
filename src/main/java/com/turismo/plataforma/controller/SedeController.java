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

    private final SedeService sedeService;

    public SedeController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    @GetMapping
    public List<SedeResponse> listar(@RequestParam(name = "q", required = false) String q) {
        return sedeService.listar(q);
    }

    @GetMapping("/{id}")
    public SedeResponse obtener(@PathVariable("id") Long id) {
        return sedeService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<SedeResponse> crear(@Valid @RequestBody SedeRequest request) {
        SedeResponse creada = sedeService.crear(request);
        return ResponseEntity.created(URI.create("/api/sedes/" + creada.idSede())).body(creada);
    }

    @PutMapping("/{id}")
    public SedeResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody SedeRequest request) {
        return sedeService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        sedeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}