package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ubicacion.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    List<Ciudad> findByDepartamentoIdDepartamentoOrderByNombreCiudad(Long idDepartamento);

    Optional<Ciudad> findByCodigoCiudadIgnoreCase(String codigoCiudad);

    /** Ciudad con su departamento y país, en una sola consulta. */
    @Query("""
           select c from Ciudad c
             join fetch c.departamento d
             join fetch d.pais
           where c.idCiudad = :idCiudad
           """)
    Optional<Ciudad> buscarConUbicacion(@Param("idCiudad") Long idCiudad);

    /** Ciudades con código (las que tienen rutas), con su país. */
    @Query("""
           select c from Ciudad c
             join fetch c.departamento d
             join fetch d.pais p
           where c.codigoCiudad is not null
           order by p.nombrePais, c.nombreCiudad
           """)
    List<Ciudad> listarConCodigo();
}
