package com.turismo.plataforma.dto.catalogo;

import com.turismo.plataforma.model.ubicacion.Ciudad;
import com.turismo.plataforma.model.ubicacion.Departamento;

/** Ciudad con su código (si tiene), departamento y país. */
public record CiudadResponse(Long idCiudad, String nombreCiudad, String codigoCiudad,
                             Long idDepartamento, String nombreDepartamento,
                             Long idPais, String nombrePais) {

    public static CiudadResponse from(Ciudad ciudad) {
        Departamento departamento = ciudad.getDepartamento();
        return new CiudadResponse(ciudad.getIdCiudad(), ciudad.getNombreCiudad(), ciudad.getCodigoCiudad(),
                departamento.getIdDepartamento(), departamento.getNombreDepartamento(),
                departamento.getPais().getIdPais(), departamento.getPais().getNombrePais());
    }
}
