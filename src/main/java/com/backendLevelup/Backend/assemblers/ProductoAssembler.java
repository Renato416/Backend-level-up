package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.ProductoDTO;
import com.backendLevelup.Backend.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoAssembler {

    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) return null;

        return new ProductoDTO(
                producto.getId(),
                producto.getName(),
                producto.getPrice(),
                producto.getImageName(),
                producto.getDescription(),
                producto.getRating()
        );
    }

    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) return null;

        return new Producto(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getImageName(),
                dto.getDescription(),
                dto.getRating()
        );
    }
}
