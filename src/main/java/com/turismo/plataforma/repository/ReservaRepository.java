package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/** Acceso a la tabla reserva. */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Todas las reservas con su cliente. También trae transacción y factura: son relaciones
     * 1 a 1 inversas que Hibernate carga siempre, y así no hace una consulta extra por reserva.
     */
    @Query("""
           select r from Reserva r
             join fetch r.cliente
             left join fetch r.transaccion t
             left join fetch t.factura
           order by r.fechaReserva desc
           """)
    List<Reserva> listarConCliente();

    /** Las reservas de un cliente. */
    @Query("""
           select r from Reserva r
             join fetch r.cliente c
             left join fetch r.transaccion t
             left join fetch t.factura
           where c.idUsuario = :idCliente
           order by r.fechaReserva desc
           """)
    List<Reserva> listarPorCliente(@Param("idCliente") Long idCliente);
}