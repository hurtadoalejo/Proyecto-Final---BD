package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.EstadoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo EstadoTransaccion. */
public interface EstadoTransaccionRepository extends JpaRepository<EstadoTransaccion, Long> {

    Optional<EstadoTransaccion> findByNombreEstadoTransaccionIgnoreCase(String nombre);

    List<EstadoTransaccion> findAllByOrderByNombreEstadoTransaccion();
}
