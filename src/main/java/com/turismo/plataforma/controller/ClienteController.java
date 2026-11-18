package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.auth.RegistroRequest;
import com.turismo.plataforma.dto.cliente.ClienteActualizarRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.dto.cliente.ClienteResumenResponse;
import com.turismo.plataforma.dto.cliente.MillasResponse;
import com.turismo.plataforma.dto.oferta.CuponResponse;
import com.turismo.plataforma.dto.reserva.ReservaResponse;
import com.turismo.plataforma.service.AuthService;
import com.turismo.plataforma.service.ClienteService;
import com.turismo.plataforma.service.OfertaService;
import com.turismo.plataforma.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/** Un cliente se crea con POST /api/clientes (panel de empleados/admin) o con POST /api/auth/registro. */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ReservaService reservaService;
    private final OfertaService ofertaService;
    private final AuthService authService;

    public ClienteController(ClienteService clienteService, ReservaService reservaService,
                             OfertaService ofertaService, AuthService authService) {
        this.clienteService = clienteService;
        this.reservaService = reservaService;
        this.ofertaService = ofertaService;
        this.authService = authService;
    }

    @GetMapping
    public List<ClienteResponse> listar(@RequestParam(name = "q", required = false) String q) {
        return clienteService.listar(q);
    }

    /** Clientes con el dinero gastado (pagos aprobados). Filtros opcionales: q, gastoMin, gastoMax. */
    @GetMapping("/resumen")
    public List<ClienteResumenResponse> resumen(@RequestParam(name = "q", required = false) String q,
                                                @RequestParam(name = "gastoMin", required = false) BigDecimal gastoMin,
                                                @RequestParam(name = "gastoMax", required = false) BigDecimal gastoMax) {
        return clienteService.listarConGasto(q, gastoMin, gastoMax);
    }

    /** Registro de un cliente desde el panel (mismos datos y validaciones que /api/auth/registro). */
    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
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
