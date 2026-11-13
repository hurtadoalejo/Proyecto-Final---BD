package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.catalogo.CatalogoItem;
import com.turismo.plataforma.dto.catalogo.CiudadResponse;
import com.turismo.plataforma.dto.catalogo.IndicativoResponse;
import com.turismo.plataforma.service.CatalogoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Listas para los desplegables del frontend (solo lectura). */
@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/paises")
    public List<CatalogoItem> paises() {
        return catalogoService.paises();
    }

    /** Para el select de indicativo: [{ idPais, nombrePais, indicativo: "+57" }]. */
    @GetMapping("/indicativos")
    public List<IndicativoResponse> indicativos() {
        return catalogoService.indicativos();
    }

    @GetMapping("/paises/{idPais}/departamentos")
    public List<CatalogoItem> departamentos(@PathVariable("idPais") Long idPais) {
        return catalogoService.departamentos(idPais);
    }

    @GetMapping("/departamentos/{idDepartamento}/ciudades")
    public List<CatalogoItem> ciudades(@PathVariable("idDepartamento") Long idDepartamento) {
        return catalogoService.ciudades(idDepartamento);
    }

    /** Ciudades con código de 3 letras: origen y destino de la búsqueda de viajes. */
    @GetMapping("/ciudades-destino")
    public List<CiudadResponse> ciudadesDestino() {
        return catalogoService.ciudadesDestino();
    }

    @GetMapping("/tipos-identificacion")
    public List<CatalogoItem> tiposIdentificacion() {
        return catalogoService.tiposIdentificacion();
    }

    @GetMapping("/tipos-transporte")
    public List<CatalogoItem> tiposTransporte() {
        return catalogoService.tiposTransporte();
    }

    @GetMapping("/tipos-trayecto")
    public List<CatalogoItem> tiposTrayecto() {
        return catalogoService.tiposTrayecto();
    }

    @GetMapping("/metodos-pago")
    public List<CatalogoItem> metodosPago() {
        return catalogoService.metodosPago();
    }

    @GetMapping("/roles")
    public List<CatalogoItem> roles() {
        return catalogoService.roles();
    }

    @GetMapping("/cargos")
    public List<CatalogoItem> cargos() {
        return catalogoService.cargos();
    }

    @GetMapping("/estados-reserva")
    public List<CatalogoItem> estadosReserva() {
        return catalogoService.estadosReserva();
    }

    @GetMapping("/estados-transaccion")
    public List<CatalogoItem> estadosTransaccion() {
        return catalogoService.estadosTransaccion();
    }

    @GetMapping("/estados-notificacion")
    public List<CatalogoItem> estadosNotificacion() {
        return catalogoService.estadosNotificacion();
    }
}
