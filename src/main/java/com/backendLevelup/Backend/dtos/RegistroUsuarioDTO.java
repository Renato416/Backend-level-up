package com.backendLevelup.Backend.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {

    private String username;
    private String email;
    private String password;
    private String address;
    private String phone;
    private LocalDate birthDate;
}
