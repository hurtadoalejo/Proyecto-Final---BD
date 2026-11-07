package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.vehiculo.VehiculoResponse;
import com.turismo.plataforma.service.VehiculoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints REST de vehículos. --> JSON */
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoResponse> listar() {
        return vehiculoService.listar();
    }

    @GetMapping("/{id}")
    public VehiculoResponse obtener(@PathVariable("id") Long id) {
        return vehiculoService.obtener(id);
    }
}