package com.backendLevelup.Backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boleta_detalle")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BoletaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boleta_id", nullable = false)
    private Boleta boleta;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Long cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Long precioUnitario;
}
