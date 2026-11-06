package com.turismo.plataforma.model;

import com.turismo.plataforma.model.enums.TipoTransporte;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehiculo")
@Getter @Setter @NoArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @NotBlank @Size(max = 20)
    @Column(name = "codigo_identificacion", nullable = false, unique = true, length = 20)
    private String codigoIdentificacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transporte", nullable = false, length = 20)
    private TipoTransporte tipoTransporte;

    @NotNull @Min(1) @Max(1000)
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;
}