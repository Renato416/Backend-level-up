package com.backendLevelup.Backend.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private LocalDate birthDate;

    private String token;
}
