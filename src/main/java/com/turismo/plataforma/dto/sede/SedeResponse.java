package com.turismo.plataforma.dto.sede;

import com.turismo.plataforma.model.Sede;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.model.ubicacion.Departamento;

public record SedeResponse(Long idSede, String nombreSede, String direccion, String pais, String departamento,
                           String ciudad, Long idCiudad) {

    public static SedeResponse from(Sede sede) {
        Ciudad ciudad = sede.getCiudad();
        Departamento departamento = ciudad.getDepartamento();
        return new SedeResponse(sede.getIdSede(), sede.getNombreSede(), sede.getDireccion(),
                departamento.getPais().getNombrePais(), departamento.getNombreDepartamento(),
                ciudad.getNombreCiudad(), ciudad.getIdCiudad());
    }
}
