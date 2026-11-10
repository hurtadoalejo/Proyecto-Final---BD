package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.reserva.ReservaRequest;
import com.turismo.plataforma.dto.reserva.ReservaResponse;
import com.turismo.plataforma.dto.reserva.ReservaResumenResponse;
import com.turismo.plataforma.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Endpoints REST de reservas. */
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /** GET /api/reservas?idCliente=1 (idCliente es opcional) */
    @GetMapping
    public List<ReservaResumenResponse> listar(@RequestParam(name = "idCliente", required = false) Long idCliente) {
        return reservaService.listar(idCliente);
    }

    /** GET /api/reservas/{id} */
    @GetMapping("/{id}")
    public ReservaResponse obtener(@PathVariable("id") Long id) {
        return reservaService.obtener(id);
    }

    /** POST /api/reservas -> 201 Created */
    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse creada = reservaService.crear(request);
        return ResponseEntity.created(URI.create("/api/reservas/" + creada.idReserva())).body(creada);
    }

    /** PUT /api/reservas/{id}/cancelar */
    @PutMapping("/{id}/cancelar")
    public ReservaResponse cancelar(@PathVariable("id") Long id) {
        return reservaService.cancelar(id);
    }

    /** DELETE /api/reservas/{id} -> 204 No Content */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}