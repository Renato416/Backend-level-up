package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.BoletaDetalleDTO;
import com.backendLevelup.Backend.model.BoletaDetalle;
import com.backendLevelup.Backend.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class BoletaDetalleAssembler {

    public BoletaDetalleDTO toDTO(BoletaDetalle detalle) {
        if (detalle == null) return null;

        BoletaDetalleDTO dto = new BoletaDetalleDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProducto() != null ? detalle.getProducto().getId() : null);
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());

        return dto;
    }

    public BoletaDetalle toEntity(BoletaDetalleDTO dto, Producto producto) {
        if (dto == null) return null;

        BoletaDetalle detalle = new BoletaDetalle();
        detalle.setId(dto.getId());
        detalle.setProducto(producto);
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());

        return detalle;
    }
}
