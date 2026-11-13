package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.oferta.OfertaRequest;
import com.turismo.plataforma.dto.oferta.OfertaResponse;
import com.turismo.plataforma.service.OfertaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    private final OfertaService servicio;

    public OfertaController(OfertaService servicio) {
        this.servicio = servicio;
    }

    /** ?vigentes=true: solo las que aplican hoy. */
    @GetMapping
    public List<OfertaResponse> listar(@RequestParam(name = "vigentes", defaultValue = "false") boolean vigentes) {
        return servicio.listar(vigentes);
    }

    @GetMapping("/{id}")
    public OfertaResponse obtener(@PathVariable("id") Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<OfertaResponse> crear(@Valid @RequestBody OfertaRequest request) {
        OfertaResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/api/ofertas/" + creado.idOferta())).body(creado);
    }

    @PutMapping("/{id}")
    public OfertaResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody OfertaRequest request) {
        return servicio.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
