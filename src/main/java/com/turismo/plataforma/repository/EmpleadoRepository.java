package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {


    @Query("""
           select e from Empleado e
             join fetch e.sede
           order by e.nombre.primerApellido, e.nombre.primerNombre
           """)
    List<Empleado> listarConSede();

    @Query("""
           select e from Empleado e
             join fetch e.sede s
           where s.idSede = :idSede
           order by e.nombre.primerApellido, e.nombre.primerNombre
           """)
    List<Empleado> listarPorSede(@Param("idSede") Long idSede);
}