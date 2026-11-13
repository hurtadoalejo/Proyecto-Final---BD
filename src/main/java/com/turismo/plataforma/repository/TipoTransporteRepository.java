package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.TipoTransporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo TipoTransporte. */
public interface TipoTransporteRepository extends JpaRepository<TipoTransporte, Long> {

    Optional<TipoTransporte> findByNombreTipoTransporteIgnoreCase(String nombre);

    List<TipoTransporte> findAllByOrderByNombreTipoTransporte();
}
