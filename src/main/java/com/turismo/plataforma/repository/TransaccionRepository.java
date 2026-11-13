package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

/** Tabla Transaccion. Su id es el de la reserva. */
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
