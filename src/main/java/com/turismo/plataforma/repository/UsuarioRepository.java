package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(String correo, Long idUsuario);

    boolean existsByIdentificacionAndIdUsuarioNot(String identificacion, Long idUsuario);
}