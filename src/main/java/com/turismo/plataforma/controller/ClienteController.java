package com.turismo.plataforma.controller;
import com.turismo.plataforma.dto.cliente.MillasResponse;

import com.turismo.plataforma.dto.cliente.ClienteActualizarRequest;
import com.turismo.plataforma.dto.cliente.ClienteRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
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

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse creado = clienteService.crear(request);
        return ResponseEntity.created(URI.create("/api/clientes/" + creado.idUsuario())).body(creado);
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