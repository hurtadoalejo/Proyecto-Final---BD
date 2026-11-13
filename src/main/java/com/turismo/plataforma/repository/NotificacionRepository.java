package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    @Query("""
           select distinct n from Notificacion n
             join fetch n.estadoNotificacion
             left join fetch n.oferta
           order by n.idNotificacion desc
           """)
    List<Notificacion> listar();

    /** Notificaciones enviadas a un cliente (tabla ClienteNotificacion). */
    @Query("""
           select distinct n from Notificacion n
             join n.clientes c
             join fetch n.estadoNotificacion
             left join fetch n.oferta
           where c.idUsuario = :idCliente
           order by n.idNotificacion desc
           """)
    List<Notificacion> listarPorCliente(@Param("idCliente") Long idCliente);
}
