package com.backendLevelup.Backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Long precio;

    @Column(name = "cantidad", nullable = false)
    private Long cantidad;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column
    private String imagenUrl;

}
