package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.EstadoNotificacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Notificacion")
@Getter @Setter @NoArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotificacion")
    private Long idNotificacion;

    @Column(name = "mensaje", nullable = false, length = 200)
    private String mensaje;

    @Column(name = "fechaEnvio")
    private LocalDateTime fechaEnvio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idEstadoNotificacion", nullable = false)
    private EstadoNotificacion estadoNotificacion;

    /** Opcional: no toda notificación lleva oferta. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOferta")
    private Oferta oferta;

    /** Relación N:M con Cliente a través de la tabla ClienteNotificacion. */
    @ManyToMany
    @JoinTable(name = "ClienteNotificacion",
            joinColumns = @JoinColumn(name = "idNotificacion"),
            inverseJoinColumns = @JoinColumn(name = "idUsuario"))
    private Set<Cliente> clientes = new LinkedHashSet<>();
}
