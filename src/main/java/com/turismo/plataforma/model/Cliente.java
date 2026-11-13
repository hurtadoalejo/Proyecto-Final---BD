package com.turismo.plataforma.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * Subclase de Usuario. millasAcumuladas es un atributo derivado:
 * se calcula con ReservaViajeRepository, no se guarda.
 */
@Entity
@Table(name = "Cliente")
@PrimaryKeyJoinColumn(name = "idUsuario")
@NoArgsConstructor
public class Cliente extends Usuario {
}
