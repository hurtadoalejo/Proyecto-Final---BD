package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.reporte.*;
import com.turismo.plataforma.exception.SolicitudInvalidaException;
import com.turismo.plataforma.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Reportes del negocio (solo lectura). Las consultas están en ReporteRepository. */
@Service
@Transactional(readOnly = true)
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final CatalogoService catalogoService;

    public ReporteService(ReporteRepository reporteRepository, CatalogoService catalogoService) {
        this.reporteRepository = reporteRepository;
        this.catalogoService = catalogoService;
    }

    public List<VentaMensualResponse> ventasMensuales(Integer anio) {
        return reporteRepository.ventasMensuales(anio);
    }

    public List<IngresoMetodoPagoResponse> ingresosPorMetodoPago() {
        return reporteRepository.ingresosPorMetodoPago();
    }

    public List<ReservasEmpleadoResponse> reservasPorEmpleado() {
        return reporteRepository.reservasPorEmpleado();
    }

    public List<RutaPasajerosResponse> rutasConMasPasajeros(int limite) {
        if (limite < 1 || limite > 100) {
            throw new SolicitudInvalidaException("El límite debe estar entre 1 y 100");
        }
        return reporteRepository.rutasConMasPasajeros(limite);
    }

    public List<ClienteGastoResponse> clientesSobrePromedio() {
        return reporteRepository.clientesSobrePromedio();
    }

    public List<RutaPasajerosResponse> rutasBajoPromedio(String tipoTransporte) {
        return reporteRepository.rutasBajoPromedio(normalizarTipo(tipoTransporte));
    }

    public List<RutaPasajerosResponse> rutasSobrePromedio(String tipoTransporte) {
        return reporteRepository.rutasSobrePromedio(normalizarTipo(tipoTransporte));
    }

    /** null o vacío = todos los tipos; si no, el nombre exacto del catálogo (AVION, BUS, TREN). */
    private String normalizarTipo(String tipoTransporte) {
        if (tipoTransporte == null || tipoTransporte.isBlank()) {
            return null;
        }
        return catalogoService.tipoTransporte(tipoTransporte).getNombreTipoTransporte();
    }
}
