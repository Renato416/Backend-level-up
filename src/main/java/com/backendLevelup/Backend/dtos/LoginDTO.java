package com.backendLevelup.Backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String correoElectronico;
    private String contraseña;
}
