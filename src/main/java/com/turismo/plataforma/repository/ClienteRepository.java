package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /** Clientes cuya identificación o primer apellido contiene el texto, con todos sus datos. */
    @Query("""
           select c from Cliente c
             join fetch c.rol
             join fetch c.tipoIdentificacion
             join fetch c.ciudad ci
             join fetch ci.departamento d
             join fetch d.pais
           where c.identificacion like concat('%', :texto, '%')
              or lower(c.nombre.primerApellido) like lower(concat('%', :texto, '%'))
           order by c.nombre.primerApellido, c.nombre.primerNombre
           """)
    List<Cliente> buscar(@Param("texto") String texto);

    /**
     * Dinero gastado por cada cliente que tiene pagos aprobados:
     * [idCliente, comprasPagadas, totalGastado].
     */
    @Query(value = """
           SELECT r.Cliente_idUsuario          AS idCliente,
                  COUNT(DISTINCT r.idReserva)  AS comprasPagadas,
                  SUM(t.montoPagado)           AS totalGastado
           FROM Reserva r
           JOIN Transaccion t        ON t.idReserva = r.idReserva
           JOIN EstadoTransaccion et ON et.idEstadoTransaccion = t.idEstadoTransaccion
           WHERE et.nombreEstadoTransaccion = 'APROBADA'
           GROUP BY r.Cliente_idUsuario
           """, nativeQuery = true)
    List<Object[]> gastoPorCliente();
}
