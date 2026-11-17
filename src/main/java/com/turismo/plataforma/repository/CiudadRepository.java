package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ubicacion.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    List<Ciudad> findByDepartamentoIdDepartamentoOrderByNombreCiudad(Long idDepartamento);

    List<Ciudad> findByNombreCiudadIgnoreCase(String nombreCiudad);

    Optional<Ciudad> findByNombreCiudadIgnoreCaseAndDepartamentoIdDepartamento(String nombreCiudad, Long idDepartamento);

    /** Ciudades que son origen o destino de alguna ruta, con su departamento y país. */
    @Query("""
           select c from Ciudad c
             join fetch c.departamento d
             join fetch d.pais p
           where exists (select 1 from Ruta r where r.ciudadOrigen = c or r.ciudadDestino = c)
           order by p.nombrePais, c.nombreCiudad
           """)
    List<Ciudad> listarConRutas();

    /** Ciudades de los países indicados, con departamento y país (para armar el catálogo de ubicaciones). */
    @Query("""
           select c from Ciudad c
             join fetch c.departamento d
             join fetch d.pais p
           where p.idPais in :idsPaises
           order by p.idPais, d.idDepartamento, c.idCiudad
           """)
    List<Ciudad> listarPorPaises(@Param("idsPaises") Collection<Long> idsPaises);

}
