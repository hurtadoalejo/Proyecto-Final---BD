package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo TipoIdentificacion. */
public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, Long> {

    Optional<TipoIdentificacion> findByNombreIdentificacionIgnoreCase(String nombre);

    List<TipoIdentificacion> findAllByOrderByNombreIdentificacion();
}
