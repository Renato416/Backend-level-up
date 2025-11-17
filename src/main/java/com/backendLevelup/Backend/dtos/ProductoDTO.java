package com.backendLevelup.Backend.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;
    private String nombre;
    private Long precio;
    private Long cantidad;
    private String imagenUrl;

    // Para evitar exponer toda la entidad Categoria, solo enviamos el nombre
    private String nombreCategoria;
}

