package com.backendLevelup.Backend.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDetalleDTO {

    private Long id;

    // No enviamos la entidad Carrito, solo su ID
    private Long carritoId;

    // No enviamos Producto entero; solo lo esencial
    private Long productoId;
    private String nombreProducto;
    private Long precioProducto;

    private Long cantidad;
}
