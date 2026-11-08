package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CuponRepository extends JpaRepository<Cupon, Long> {

    List<Cupon> findAllByOrderByFechaInicioDesc();

    List<Cupon> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaFin(
            LocalDate fechaInicioHasta, LocalDate fechaFinDesde);
}