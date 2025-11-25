package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.CarritoDetalleDTO;
import com.backendLevelup.Backend.model.CarritoDetalle;
import com.backendLevelup.Backend.model.Carrito;
import org.springframework.stereotype.Component;

@Component
public class CarritoDetalleAssembler {

    // Convierte de entidad a DTO
    public CarritoDetalleDTO toDTO(CarritoDetalle detalle) {
        if (detalle == null) return null;

        CarritoDetalleDTO dto = new CarritoDetalleDTO();
        dto.setId(detalle.getId());
        dto.setCarritoId(detalle.getCarrito() != null ? detalle.getCarrito().getId() : null);
        dto.setProductoId(detalle.getProducto() != null ? detalle.getProducto().getId() : null);
        dto.setNombreProducto(detalle.getProducto() != null ? detalle.getProducto().getNombre() : null);
        dto.setPrecioProducto(detalle.getProducto() != null ? detalle.getProducto().getPrecio() : null);
        dto.setCantidad(detalle.getCantidad());

        return dto;
    }

    // Convierte de DTO a entidad
    public CarritoDetalle toEntity(CarritoDetalleDTO dto, Carrito carrito) {
        if (dto == null) return null;

        CarritoDetalle detalle = new CarritoDetalle();
        detalle.setId(dto.getId());
        detalle.setCarrito(carrito);
        detalle.setCantidad(dto.getCantidad());

        return detalle;
    }


}
