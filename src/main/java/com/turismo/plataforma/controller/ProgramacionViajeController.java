package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.programacion.ProgramacionViajeRequest;
import com.turismo.plataforma.dto.programacion.ProgramacionViajeResponse;
import com.turismo.plataforma.service.ProgramacionViajeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/programaciones")
public class ProgramacionViajeController {

    private final ProgramacionViajeService programacionService;

    public ProgramacionViajeController(ProgramacionViajeService programacionService) {
        this.programacionService = programacionService;
    }

    @GetMapping
    public List<ProgramacionViajeResponse> listar(@RequestParam(name = "idRuta", required = false) Long idRuta) {
        return programacionService.listar(idRuta);
    }

    @GetMapping("/{id}")
    public ProgramacionViajeResponse obtener(@PathVariable("id") Long id) {
        return programacionService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ProgramacionViajeResponse> crear(@Valid @RequestBody ProgramacionViajeRequest request) {
        ProgramacionViajeResponse creada = programacionService.crear(request);
        return ResponseEntity.created(URI.create("/api/programaciones/" + creada.idProgramacion())).body(creada);
    }

    @PutMapping("/{id}")
    public ProgramacionViajeResponse actualizar(@PathVariable("id") Long id,
                                                @Valid @RequestBody ProgramacionViajeRequest request) {
        return programacionService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        programacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}