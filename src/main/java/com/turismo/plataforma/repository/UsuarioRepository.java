package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/** Tabla Usuario (común a clientes y empleados). */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /** Para el login: trae el usuario con su rol. */
    @Query("select u from Usuario u join fetch u.rol where lower(u.correo) = lower(:correo)")
    Optional<Usuario> buscarPorCorreo(@Param("correo") String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(String correo, Long idUsuario);

    boolean existsByIdentificacionAndIdUsuarioNot(String identificacion, Long idUsuario);

    boolean existsByRolNombreRol(String nombreRol);

    long countByRolNombreRol(String nombreRol);
}
