package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.assemblers.ProductoAssembler;
import com.backendLevelup.Backend.dtos.ProductoDTO;
import com.backendLevelup.Backend.exceptions.ResourceNotFoundException;
import com.backendLevelup.Backend.model.Producto;
import com.backendLevelup.Backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoAssembler productoAssembler;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               ProductoAssembler productoAssembler) {
        this.productoRepository = productoRepository;
        this.productoAssembler = productoAssembler;
    }

    // ---------------------------
    // CREAR
    // ---------------------------
    @Override
    public ProductoDTO crearProducto(ProductoDTO dto) {

        Producto producto = new Producto();
        producto.setName(dto.getName());
        producto.setPrice(dto.getPrice());
        producto.setImageName(dto.getImageName());
        producto.setDescription(dto.getDescription());
        producto.setRating(dto.getRating());

        Producto guardado = productoRepository.save(producto);
        return productoAssembler.toDTO(guardado);
    }

    // ---------------------------
    // OBTENER POR ID
    // ---------------------------
    @Override
    public ProductoDTO obtenerProductoPorId(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con ID: " + id)
                );

        return productoAssembler.toDTO(producto);
    }

    // ---------------------------
    // LISTAR
    // ---------------------------
    @Override
    public List<ProductoDTO> listarProductos() {

        return productoRepository.findAll()
                .stream()
                .map(productoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------
    // ACTUALIZAR
    // ---------------------------
    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con ID: " + id)
                );

        producto.setName(dto.getName());
        producto.setPrice(dto.getPrice());
        producto.setImageName(dto.getImageName());
        producto.setDescription(dto.getDescription());
        producto.setRating(dto.getRating());

        Producto actualizado = productoRepository.save(producto);
        return productoAssembler.toDTO(actualizado);
    }

    // ---------------------------
    // ELIMINAR
    // ---------------------------
    @Override
    public void eliminarProducto(Long id) {

        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }

        productoRepository.deleteById(id);
    }
}
