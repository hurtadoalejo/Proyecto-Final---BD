package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.reserva.AlternativaFechaResponse;
import com.turismo.plataforma.dto.reserva.CambioFechaRequest;
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

    /** El cliente cancela su reserva (?idCliente= su id). Solo si faltan 5 días o más para el viaje. */
    @PutMapping("/{id}/cancelar")
    public ReservaResponse cancelar(@PathVariable("id") Long id,
                                    @RequestParam(name = "idCliente", required = false) Long idCliente) {
        return reservaService.cancelar(id, idCliente);
    }

    /** Otras salidas de la misma ruta a las que el cliente puede mover este viaje de su reserva. */
    @GetMapping("/{id}/viajes/{idProgramacionViaje}/alternativas")
    public List<AlternativaFechaResponse> alternativasDeFecha(@PathVariable("id") Long id,
                                                             @PathVariable("idProgramacionViaje") Long idProgramacionViaje,
                                                             @RequestParam(name = "idCliente", required = false) Long idCliente) {
        return reservaService.alternativasDeFecha(id, idProgramacionViaje, idCliente);
    }

    /**
     * El cliente cambia la fecha de un viaje de su reserva (?idCliente= su id): la fila de
     * ReservaViaje pasa a apuntar a otra salida de la misma ruta. Solo con 5 días o más de anticipación.
     */
    @PutMapping("/{id}/viajes/{idProgramacionViaje}")
    public ReservaResponse cambiarFecha(@PathVariable("id") Long id,
                                        @PathVariable("idProgramacionViaje") Long idProgramacionViaje,
                                        @RequestParam(name = "idCliente", required = false) Long idCliente,
                                        @Valid @RequestBody CambioFechaRequest request) {
        return reservaService.cambiarFecha(id, idProgramacionViaje, request.idProgramacionViajeNuevo(), idCliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
