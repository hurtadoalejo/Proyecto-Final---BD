package com.turismo.plataforma.model;

import com.turismo.plataforma.model.ubicacion.Ciudad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/** asientosDisponibles es derivado: se calcula por programación (capacidad - ocupados). */
@Entity
@Table(name = "Ruta")
@Getter @Setter @NoArgsConstructor
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuta")
    private Long idRuta;

    /** Kilómetros. */
    @Column(name = "distancia", nullable = false, precision = 10, scale = 2)
    private BigDecimal distancia;

    /** Minutos. */
    @Column(name = "duracionEstimada", nullable = false)
    private Integer duracionEstimada;

    @Column(name = "precioBase", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioBase;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCiudadOrigen", nullable = false)
    private Ciudad ciudadOrigen;

    @Column(name = "direccionOrigen", length = 40)
    private String direccionOrigen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCiudadDestino", nullable = false)
    private Ciudad ciudadDestino;

    @Column(name = "direccionDestino", length = 40)
    private String direccionDestino;

    /** Vehículo que cubre la ruta (tabla RutaVehiculo, PK = idRuta). */
    @OneToOne(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private RutaVehiculo rutaVehiculo;

    /** Asigna o cambia el vehículo de la ruta. */
    public void asignarVehiculo(Vehiculo vehiculo) {
        if (rutaVehiculo == null) {
            rutaVehiculo = new RutaVehiculo();
            rutaVehiculo.setRuta(this);
        }
        rutaVehiculo.setVehiculo(vehiculo);
    }

    public Vehiculo getVehiculo() {
        return rutaVehiculo == null ? null : rutaVehiculo.getVehiculo();
    }
}
