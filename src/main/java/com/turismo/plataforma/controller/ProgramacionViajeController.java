package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.programacion.*;
import com.turismo.plataforma.service.ProgramacionViajeService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/programaciones")
public class ProgramacionViajeController {

    private final ProgramacionViajeService programacionViajeService;

    public ProgramacionViajeController(ProgramacionViajeService programacionViajeService) {
        this.programacionViajeService = programacionViajeService;
    }

    /** ?idRuta=&desde=2026-12-01&hasta=2026-12-07 (todos opcionales; por defecto los próximos 7 días). */
    @GetMapping
    public List<ProgramacionResponse> listar(
            @RequestParam(name = "idRuta", required = false) Long idRuta,
            @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return programacionViajeService.listar(idRuta, desde, hasta);
    }

    @GetMapping("/{id}")
    public ProgramacionResponse obtener(@PathVariable("id") Long id) {
        return programacionViajeService.obtener(id);
    }

    @GetMapping("/{id}/asientos")
    public DisponibilidadResponse disponibilidad(@PathVariable("id") Long id) {
        return programacionViajeService.disponibilidad(id);
    }

    @PostMapping
    public ResponseEntity<ProgramacionResponse> crear(@Valid @RequestBody ProgramacionRequest request) {
        ProgramacionResponse creada = programacionViajeService.crear(request);
        return ResponseEntity.created(URI.create("/api/programaciones/" + creada.idProgramacionViaje())).body(creada);
    }

    /** Repite una ruta cada N horas entre dos fechas. */
    @PostMapping("/recurrente")
    @ResponseStatus(HttpStatus.CREATED)
    public ProgramacionRecurrenteResponse crearRecurrente(@Valid @RequestBody ProgramacionRecurrenteRequest request) {
        return programacionViajeService.crearRecurrente(request);
    }

    @PutMapping("/{id}")
    public ProgramacionResponse actualizar(@PathVariable("id") Long id,
                                           @Valid @RequestBody ProgramacionRequest request) {
        return programacionViajeService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        programacionViajeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
