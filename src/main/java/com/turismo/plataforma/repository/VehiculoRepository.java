package com.turismo.plataforma.repository;
import java.util.List;

import com.turismo.plataforma.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByCodigoIdentificacion(String codigoIdentificacion);

    boolean existsByCodigoIdentificacionAndIdVehiculoNot(String codigoIdentificacion, Long idVehiculo);

    List<Vehiculo> findByCodigoIdentificacionContainingIgnoreCaseOrderByCodigoIdentificacion(String texto);
}