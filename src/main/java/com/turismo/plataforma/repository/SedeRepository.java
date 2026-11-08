package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SedeRepository extends JpaRepository<Sede, Long> {

    List<Sede> findByNombreSedeContainingIgnoreCaseOrderByNombreSede(String texto);

    boolean existsByNombreSedeIgnoreCase(String nombreSede);

    boolean existsByNombreSedeIgnoreCaseAndIdSedeNot(String nombreSede, Long idSede);
}