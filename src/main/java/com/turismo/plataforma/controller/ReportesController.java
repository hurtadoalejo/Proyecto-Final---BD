package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.reporte.*;
import com.turismo.plataforma.service.ReporteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Reportes para tablas y gráficos del frontend. Todos son GET y devuelven listas JSON. */
@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ReporteService reporteService;

    public ReportesController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /** Ventas aprobadas mes a mes. ?anio=2026 opcional. */
    @GetMapping("/ventas-mensuales")
    public List<VentaMensualResponse> ventasMensuales(@RequestParam(name = "anio", required = false) Integer anio) {
        return reporteService.ventasMensuales(anio);
    }

    /** Ingresos aprobados por método de pago, con porcentaje del total. */
    @GetMapping("/ingresos-metodo-pago")
    public List<IngresoMetodoPagoResponse> ingresosPorMetodoPago() {
        return reporteService.ingresosPorMetodoPago();
    }

    /** En cuántas reservas ha participado cada empleado. */
    @GetMapping("/reservas-por-empleado")
    public List<ReservasEmpleadoResponse> reservasPorEmpleado() {
        return reporteService.reservasPorEmpleado();
    }

    /** Rutas con más pasajeros (detalles de reserva). ?limite=10 por defecto. */
    @GetMapping("/rutas-mas-pasajeros")
    public List<RutaPasajerosResponse> rutasConMasPasajeros(
            @RequestParam(name = "limite", defaultValue = "10") int limite) {
        return reporteService.rutasConMasPasajeros(limite);
    }

    /** Clientes que han gastado más que el promedio de todos los clientes. */
    @GetMapping("/clientes-sobre-promedio")
    public List<ClienteGastoResponse> clientesSobrePromedio() {
        return reporteService.clientesSobrePromedio();
    }

    /** Rutas menos eficientes: menos pasajeros que el promedio de su tipo. ?tipo=AVION|BUS|TREN opcional. */
    @GetMapping("/rutas-bajo-promedio")
    public List<RutaPasajerosResponse> rutasBajoPromedio(@RequestParam(name = "tipo", required = false) String tipo) {
        return reporteService.rutasBajoPromedio(tipo);
    }

    /** Rutas más vendidas de cada tipo: más pasajeros que el promedio de su tipo. ?tipo= opcional. */
    @GetMapping("/rutas-sobre-promedio")
    public List<RutaPasajerosResponse> rutasSobrePromedio(@RequestParam(name = "tipo", required = false) String tipo) {
        return reporteService.rutasSobrePromedio(tipo);
    }
}
