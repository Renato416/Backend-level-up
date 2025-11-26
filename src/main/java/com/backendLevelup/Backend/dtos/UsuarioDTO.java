package com.backendLevelup.Backend.dtos;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombreUsuario;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String run;
    private String rol;
    private String correoElectronico;

    // El campo para llevar la llave de acceso
    private String token;
}