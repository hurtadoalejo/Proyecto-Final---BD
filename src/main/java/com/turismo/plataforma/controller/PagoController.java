package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.pago.PagoRequest;
import com.turismo.plataforma.dto.pago.PagoResponse;
import com.turismo.plataforma.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** El pago es una entidad débil de la reserva: vive en /api/reservas/{id}/pago. */
@RestController
@RequestMapping("/api/reservas/{idReserva}/pago")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public PagoResponse obtener(@PathVariable("idReserva") Long idReserva) {
        return pagoService.obtener(idReserva);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponse pagar(@PathVariable("idReserva") Long idReserva, @Valid @RequestBody PagoRequest request) {
        return pagoService.pagar(idReserva, request);
    }
}
