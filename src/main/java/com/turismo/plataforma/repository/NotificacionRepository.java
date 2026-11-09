package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    @Query("""
           select n from Notificacion n
             join fetch n.cliente
             left join fetch n.cupon
           order by n.idNotificacion desc
           """)
    List<Notificacion> listarConClienteYCupon();

    @Query("""
           select n from Notificacion n
             join fetch n.cliente c
             left join fetch n.cupon
           where c.idUsuario = :idCliente
           order by n.idNotificacion desc
           """)
    List<Notificacion> listarPorCliente(@Param("idCliente") Long idCliente);
}