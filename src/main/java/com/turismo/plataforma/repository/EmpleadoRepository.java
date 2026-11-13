package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /** Empleados con cargo, sede y ubicación; si idSede es null, todos. */
    @Query("""
           select e from Empleado e
             join fetch e.rol
             join fetch e.tipoIdentificacion
             join fetch e.cargo
             join fetch e.sede s
             join fetch e.ciudad ci
             join fetch ci.departamento d
             join fetch d.pais
           where :idSede is null or s.idSede = :idSede
           order by e.nombre.primerApellido, e.nombre.primerNombre
           """)
    List<Empleado> listar(@Param("idSede") Long idSede);
}
