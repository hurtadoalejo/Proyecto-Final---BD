package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.catalogo.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Catálogo Rol. */
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreRolIgnoreCase(String nombre);

    List<Rol> findAllByOrderByNombreRol();
}
