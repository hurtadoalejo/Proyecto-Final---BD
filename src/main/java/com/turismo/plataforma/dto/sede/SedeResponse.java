package com.turismo.plataforma.dto.sede;

import com.turismo.plataforma.model.Sede;

public record SedeResponse(Long idSede, String nombreSede, String direccion) {

    public static SedeResponse from(Sede sede) {
        return new SedeResponse(sede.getIdSede(), sede.getNombreSede(), sede.getDireccion());
    }
}