package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OfertaRepository extends JpaRepository<Oferta, Long> {

    List<Oferta> findAllByOrderByFechaInicioDesc();

    /** Vigentes: fechaInicio <= hoy y fechaFin >= hoy (se pasa la misma fecha dos veces). */
    List<Oferta> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaFin(LocalDate hoy1, LocalDate hoy2);
}
