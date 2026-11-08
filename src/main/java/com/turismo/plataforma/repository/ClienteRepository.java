package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("""
           select c from Cliente c
           where c.identificacion like concat('%', :texto, '%')
              or lower(c.nombre.primerApellido) like lower(concat('%', :texto, '%'))
           order by c.nombre.primerApellido, c.nombre.primerNombre
           """)
    List<Cliente> buscar(@Param("texto") String texto);
}