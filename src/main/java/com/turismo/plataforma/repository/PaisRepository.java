package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ubicacion.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaisRepository extends JpaRepository<Pais, Long> {

    List<Pais> findAllByOrderByNombrePais();

    Optional<Pais> findByNombrePaisIgnoreCase(String nombrePais);

    List<Pais> findByContinenteIdContinenteOrderByNombrePais(Long idContinente);

    /** Países de un continente (en el orden en que se cargaron). */
    List<Pais> findByContinenteNombreContinenteOrderByIdPais(String nombreContinente);

    /** Países que NO son de ese continente. */
    List<Pais> findByContinenteNombreContinenteNotOrderByIdPais(String nombreContinente);
}
