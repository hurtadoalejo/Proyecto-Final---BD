package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    /** Rutas cuyo origen o destino (nombre o código de ciudad) contiene el texto, con todos sus datos. */
    @Query("""
           select r from Ruta r
             join fetch r.ciudadOrigen co
             join fetch r.ciudadDestino cd
             left join fetch r.rutaVehiculo rv
             left join fetch rv.vehiculo v
             left join fetch v.tipoTransporte
           where lower(co.nombreCiudad) like lower(concat('%', :texto, '%'))
              or lower(cd.nombreCiudad) like lower(concat('%', :texto, '%'))
              or lower(co.codigoCiudad) = lower(:texto)
              or lower(cd.codigoCiudad) = lower(:texto)
           order by co.nombreCiudad, cd.nombreCiudad
           """)
    List<Ruta> buscar(@Param("texto") String texto);
}
