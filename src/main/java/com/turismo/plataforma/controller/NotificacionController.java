package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.notificacion.NotificacionRequest;
import com.turismo.plataforma.dto.notificacion.NotificacionResponse;
import com.turismo.plataforma.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<NotificacionResponse> listar(@RequestParam(name = "idCliente", required = false) Long idCliente) {
        return notificacionService.listar(idCliente);
    }

    @GetMapping("/{id}")
    public NotificacionResponse obtener(@PathVariable("id") Long id) {
        return notificacionService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<NotificacionResponse> crear(@Valid @RequestBody NotificacionRequest request) {
        NotificacionResponse creada = notificacionService.crear(request);
        return ResponseEntity.created(URI.create("/api/notificaciones/" + creada.idNotificacion())).body(creada);
    }

    @PutMapping("/{id}")
    public NotificacionResponse actualizar(@PathVariable("id") Long id,
                                           @Valid @RequestBody NotificacionRequest request) {
        return notificacionService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}