package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo EstadoReserva. */
public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Long> {

    Optional<EstadoReserva> findByNombreEstadoReservaIgnoreCase(String nombre);

    List<EstadoReserva> findAllByOrderByNombreEstadoReserva();
}
