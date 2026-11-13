package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    @Query("""
           select v from Vehiculo v
             join fetch v.tipoTransporte
           where lower(v.codigoIdentificacion) like lower(concat('%', :texto, '%'))
           order by v.codigoIdentificacion
           """)
    List<Vehiculo> buscar(@Param("texto") String texto);

    boolean existsByCodigoIdentificacionIgnoreCase(String codigo);

    boolean existsByCodigoIdentificacionIgnoreCaseAndIdVehiculoNot(String codigo, Long idVehiculo);
}
