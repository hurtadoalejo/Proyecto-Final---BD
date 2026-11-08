package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.cupon.CuponRequest;
import com.turismo.plataforma.dto.cupon.CuponResponse;
import com.turismo.plataforma.service.CuponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cupones")
public class CuponController {

    private final CuponService cuponService;

    public CuponController(CuponService cuponService) {
        this.cuponService = cuponService;
    }

    @GetMapping
    public List<CuponResponse> listar(@RequestParam(name = "vigentes", defaultValue = "false") boolean vigentes) {
        return cuponService.listar(vigentes);
    }

    @GetMapping("/{id}")
    public CuponResponse obtener(@PathVariable("id") Long id) {
        return cuponService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<CuponResponse> crear(@Valid @RequestBody CuponRequest request) {
        CuponResponse creado = cuponService.crear(request);
        return ResponseEntity.created(URI.create("/api/cupones/" + creado.idCupon())).body(creado);
    }

    @PutMapping("/{id}")
    public CuponResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody CuponRequest request) {
        return cuponService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        cuponService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}