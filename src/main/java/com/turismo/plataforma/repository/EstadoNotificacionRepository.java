package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.EstadoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo EstadoNotificacion. */
public interface EstadoNotificacionRepository extends JpaRepository<EstadoNotificacion, Long> {

    Optional<EstadoNotificacion> findByNombreEstadoNotificacionIgnoreCase(String nombre);

    List<EstadoNotificacion> findAllByOrderByNombreEstadoNotificacion();
}
