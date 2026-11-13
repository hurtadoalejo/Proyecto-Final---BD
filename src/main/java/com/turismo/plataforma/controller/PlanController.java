package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.plan.PlanRequest;
import com.turismo.plataforma.dto.plan.PlanResponse;
import com.turismo.plataforma.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/planes")
public class PlanController {

    private final PlanService servicio;

    public PlanController(PlanService servicio) {
        this.servicio = servicio;
    }

    /** Filtro opcional ?q= */
    @GetMapping
    public List<PlanResponse> listar(@RequestParam(name = "q", required = false) String q) {
        return servicio.listar(q);
    }

    @GetMapping("/{id}")
    public PlanResponse obtener(@PathVariable("id") Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<PlanResponse> crear(@Valid @RequestBody PlanRequest request) {
        PlanResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/api/planes/" + creado.idPlan())).body(creado);
    }

    @PutMapping("/{id}")
    public PlanResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody PlanRequest request) {
        return servicio.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
