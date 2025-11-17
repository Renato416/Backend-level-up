package com.backendLevelup.Backend.dtos;

import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {

    private Long id;

    // En lugar de exponer toda la entidad Usuario, solo enviamos el ID
    private Long usuarioId;

    // Lista de detalles del carrito
    private List<CarritoDetalleDTO> detalles;
}
