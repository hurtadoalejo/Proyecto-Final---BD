package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ubicacion.Continente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContinenteRepository extends JpaRepository<Continente, Long> {

    List<Continente> findAllByOrderByIdContinente();
}
