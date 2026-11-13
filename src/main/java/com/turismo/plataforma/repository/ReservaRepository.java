package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /** Resumen de reservas (de un cliente, o todas si idCliente es null). */
    @Query("""
           select r from Reserva r
             join fetch r.cliente c
             join fetch r.estadoReserva
             left join fetch r.transaccion t
             left join fetch t.factura
           where :idCliente is null or c.idUsuario = :idCliente
           order by r.fechaReserva desc
           """)
    List<Reserva> listar(@Param("idCliente") Long idCliente);
}
