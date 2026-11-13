package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ubicacion.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    List<Departamento> findByPaisIdPaisOrderByNombreDepartamento(Long idPais);
}
