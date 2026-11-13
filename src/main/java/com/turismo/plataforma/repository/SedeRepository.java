package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SedeRepository extends JpaRepository<Sede, Long> {

    @Query("""
           select s from Sede s
             join fetch s.ciudad
           where lower(s.nombreSede) like lower(concat('%', :texto, '%'))
           order by s.nombreSede
           """)
    List<Sede> buscar(@Param("texto") String texto);

    Optional<Sede> findByNombreSedeIgnoreCase(String nombreSede);

    boolean existsByNombreSedeIgnoreCase(String nombreSede);

    boolean existsByNombreSedeIgnoreCaseAndIdSedeNot(String nombreSede, Long idSede);
}
