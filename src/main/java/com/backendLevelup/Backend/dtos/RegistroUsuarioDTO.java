package com.backendLevelup.Backend.dtos;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {

    private String nombreUsuario;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String contraseña;
    private String run;
    private String correoElectronico;
}
