package com.backendLevelup.Backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boleta")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime fecha;

    @Column(nullable = false)
    private Long total;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoletaDetalle> detalles = new ArrayList<>();
}
