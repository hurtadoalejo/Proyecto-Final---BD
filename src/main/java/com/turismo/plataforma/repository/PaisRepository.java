package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ubicacion.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaisRepository extends JpaRepository<Pais, Long> {

    List<Pais> findAllByOrderByNombrePais();
}
