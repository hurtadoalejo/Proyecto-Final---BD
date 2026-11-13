package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ubicacion.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    List<Ciudad> findByDepartamentoIdDepartamentoOrderByNombreCiudad(Long idDepartamento);

    Optional<Ciudad> findByCodigoCiudadIgnoreCase(String codigoCiudad);

    Optional<Ciudad> findByNombreCiudadIgnoreCaseAndDepartamentoIdDepartamento(String nombreCiudad, Long idDepartamento);

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
