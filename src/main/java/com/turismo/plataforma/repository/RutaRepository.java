package com.turismo.plataforma.repository;
import java.util.List;

import com.turismo.plataforma.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    List<Ruta> findByDireccionOrigenContainingIgnoreCaseOrDireccionDestinoContainingIgnoreCaseOrderByDireccionOrigen(
            String textoOrigen, String textoDestino);
}