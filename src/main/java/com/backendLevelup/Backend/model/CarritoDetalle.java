package com.backendLevelup.Backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrito_detalle")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Long cantidad;
}
