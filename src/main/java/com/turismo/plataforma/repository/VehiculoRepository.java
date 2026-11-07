package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}