package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.TipoTrayecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo TipoTrayecto. */
public interface TipoTrayectoRepository extends JpaRepository<TipoTrayecto, Long> {

    Optional<TipoTrayecto> findByNombreTipoTrayectoIgnoreCase(String nombre);

    List<TipoTrayecto> findAllByOrderByNombreTipoTrayecto();
}
