package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.pago.PagoRequest;
import com.turismo.plataforma.dto.pago.PagoResponse;
import com.turismo.plataforma.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/** Pago y factura de una reserva. */
@RestController
@RequestMapping("/api/reservas/{idReserva}/pago")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    /** GET /api/reservas/{idReserva}/pago */
    @GetMapping
    public PagoResponse obtener(@PathVariable("idReserva") Long idReserva) {
        return pagoService.obtener(idReserva);
    }

    /** POST /api/reservas/{idReserva}/pago -> 201 Created */
    @PostMapping
    public ResponseEntity<PagoResponse> pagar(@PathVariable("idReserva") Long idReserva,
                                              @Valid @RequestBody PagoRequest request) {
        PagoResponse pago = pagoService.pagar(idReserva, request);
        return ResponseEntity.created(URI.create("/api/reservas/" + idReserva + "/pago")).body(pago);
    }
}