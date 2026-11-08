package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.ruta.RutaResponse;
import com.turismo.plataforma.service.RutaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    public List<RutaResponse> listar() {
        return rutaService.listar();
    }

    @GetMapping("/{id}")
    public RutaResponse obtener(@PathVariable("id") Long id) {
        return rutaService.obtener(id);
    }
}