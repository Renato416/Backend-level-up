package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.ProductoDTO;
import com.backendLevelup.Backend.model.Categoria;
import com.backendLevelup.Backend.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoAssembler {

    // Convierte de entidad a DTO
    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setCantidad(producto.getCantidad());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setNombreCategoria(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null);

        return dto;
    }

    // Convierte de DTO a entidad
    public Producto toEntity(ProductoDTO dto, Categoria categoria) {
        if (dto == null) return null;

        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria); // Necesitamos pasar la categoría ya resuelta

        return producto;
    }
}
