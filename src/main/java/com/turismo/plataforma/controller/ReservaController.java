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

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /** ?idCliente= opcional: reservas de un cliente. */
    @GetMapping
    public List<ReservaResumenResponse> listar(@RequestParam(name = "idCliente", required = false) Long idCliente) {
        return reservaService.listar(idCliente);
    }

    @GetMapping("/{id}")
    public ReservaResponse obtener(@PathVariable("id") Long id) {
        return reservaService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse creada = reservaService.crear(request);
        return ResponseEntity.created(URI.create("/api/reservas/" + creada.idReserva())).body(creada);
    }

    @PutMapping("/{id}/cancelar")
    public ReservaResponse cancelar(@PathVariable("id") Long id) {
        return reservaService.cancelar(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
