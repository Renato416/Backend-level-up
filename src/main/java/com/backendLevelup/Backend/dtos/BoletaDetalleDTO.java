package com.backendLevelup.Backend.dtos;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoletaDetalleDTO {

    private Long id;
    private Long productoId;
    private Long cantidad;
    private Long precioUnitario;
}
