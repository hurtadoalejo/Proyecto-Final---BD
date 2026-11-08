package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.cupon.CuponRequest;
import com.turismo.plataforma.dto.cupon.CuponResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Cupon;
import com.turismo.plataforma.repository.CuponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CuponService {

    private final CuponRepository cuponRepository;

    public CuponService(CuponRepository cuponRepository) {
        this.cuponRepository = cuponRepository;
    }

    public List<CuponResponse> listar(boolean soloVigentes) {
        List<Cupon> cupones;
        if (soloVigentes) {
            LocalDate hoy = LocalDate.now();
            cupones = cuponRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaFin(hoy, hoy);
        } else {
            cupones = cuponRepository.findAllByOrderByFechaInicioDesc();
        }
        return cupones.stream()
                .map(CuponResponse::from)
                .toList();
    }

    public CuponResponse obtener(Long id) {
        return CuponResponse.from(buscarEntidad(id));
    }

    @Transactional
    public CuponResponse crear(CuponRequest request) {
        validarFechas(request);
        Cupon cupon = new Cupon();
        aplicarDatos(cupon, request);
        return CuponResponse.from(cuponRepository.save(cupon));
    }

    @Transactional
    public CuponResponse actualizar(Long id, CuponRequest request) {
        Cupon cupon = buscarEntidad(id);
        validarFechas(request);
        aplicarDatos(cupon, request);
        return CuponResponse.from(cupon);
    }

    @Transactional
    public void eliminar(Long id) {
        Cupon cupon = buscarEntidad(id);
        cuponRepository.delete(cupon);
        cuponRepository.flush();
    }

    private Cupon buscarEntidad(Long id) {
        return cuponRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cupón con id " + id));
    }

    private void validarFechas(CuponRequest request) {
        if (request.fechaFin().isBefore(request.fechaInicio())) {
            throw new ConflictoException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private void aplicarDatos(Cupon cupon, CuponRequest request) {
        cupon.setTitulo(request.titulo().trim());
        cupon.setDescripcion(request.descripcion());
        cupon.setFechaInicio(request.fechaInicio());
        cupon.setFechaFin(request.fechaFin());
        cupon.setPorcentajeDescuento(request.porcentajeDescuento());
    }
}