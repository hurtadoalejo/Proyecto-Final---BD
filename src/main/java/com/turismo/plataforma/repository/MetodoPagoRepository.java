package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo MetodoPago. */
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {

    Optional<MetodoPago> findByNombreMetodoPagoIgnoreCase(String nombre);

    List<MetodoPago> findAllByOrderByNombreMetodoPago();
}
