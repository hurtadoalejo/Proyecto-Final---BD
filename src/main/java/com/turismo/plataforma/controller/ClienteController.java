package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.cliente.ClienteActualizarRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.dto.cliente.MillasResponse;
import com.turismo.plataforma.dto.oferta.CuponResponse;
import com.turismo.plataforma.dto.reserva.ReservaResponse;
import com.turismo.plataforma.service.ClienteService;
import com.turismo.plataforma.service.OfertaService;
import com.turismo.plataforma.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Para crear un cliente se usa POST /api/auth/registro. */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ReservaService reservaService;
    private final OfertaService ofertaService;

    public ClienteController(ClienteService clienteService, ReservaService reservaService,
                             OfertaService ofertaService) {
        this.clienteService = clienteService;
        this.reservaService = reservaService;
        this.ofertaService = ofertaService;
    }

    @GetMapping
    public List<ClienteResponse> listar(@RequestParam(name = "q", required = false) String q) {
        return clienteService.listar(q);
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable("id") Long id) {
        return clienteService.obtener(id);
    }

    @GetMapping("/{id}/millas")
    public MillasResponse millas(@PathVariable("id") Long id) {
        return clienteService.calcularMillas(id);
    }

    /** Historial de compras (reservas con viajes, pasajeros, valores y pago). */
    @GetMapping("/{id}/compras")
    public List<ReservaResponse> compras(@PathVariable("id") Long id) {
        return reservaService.comprasDeCliente(id);
    }

    /** Cupones disponibles: ofertas vigentes que le llegaron por notificación. */
    @GetMapping("/{id}/cupones")
    public List<CuponResponse> cupones(@PathVariable("id") Long id) {
        return ofertaService.cuponesDeCliente(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable("id") Long id,
                                      @Valid @RequestBody ClienteActualizarRequest request) {
        return clienteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
