package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.dtos.ProductoDTO;
import java.util.List;

public interface ProductoService {

    ProductoDTO crearProducto(ProductoDTO dto);

    ProductoDTO obtenerProductoPorId(Long id);

    List<ProductoDTO> listarProductos();

    ProductoDTO actualizarProducto(Long id, ProductoDTO dto);

    void eliminarProducto(Long id);
}
