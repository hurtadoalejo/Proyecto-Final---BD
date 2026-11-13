package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo Cargo. */
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findByNombreCargoIgnoreCase(String nombre);

    List<Cargo> findAllByOrderByNombreCargo();
}
