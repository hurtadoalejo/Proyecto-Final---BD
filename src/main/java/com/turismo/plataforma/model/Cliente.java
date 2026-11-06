package com.turismo.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter @Setter @NoArgsConstructor
public class Cliente extends Usuario {

    @OneToMany(mappedBy = "cliente")
    private List<Notificacion> notificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<Reserva> reservas = new ArrayList<>();
}