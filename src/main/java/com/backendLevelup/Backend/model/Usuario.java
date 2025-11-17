package com.backendLevelup.Backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "dirección")
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "contraseña", nullable = false)
    private String contraseña;

    @Column(name = "run", unique = true)
    private String run;

    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;
}
