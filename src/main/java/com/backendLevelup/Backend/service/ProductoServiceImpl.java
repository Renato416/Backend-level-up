package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.assemblers.ProductoAssembler;
import com.backendLevelup.Backend.dtos.ProductoDTO;
import com.backendLevelup.Backend.exceptions.ResourceNotFoundException;
import com.backendLevelup.Backend.model.Categoria;
import com.backendLevelup.Backend.model.Producto;
import com.backendLevelup.Backend.repository.CategoriaRepository;
import com.backendLevelup.Backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoAssembler productoAssembler;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               CategoriaRepository categoriaRepository,
                               ProductoAssembler productoAssembler) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoAssembler = productoAssembler;
    }

    @Override
    public ProductoDTO crearProducto(ProductoDTO dto) {
        Categoria categoria = categoriaRepository.findByNombre(dto.getNombreCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria);

        Producto guardado = productoRepository.save(producto);
        return productoAssembler.toDTO(guardado);
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        return productoAssembler.toDTO(producto);
    }

    @Override
    public List<ProductoDTO> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(productoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findByNombre(dto.getNombreCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria);

        Producto actualizado = productoRepository.save(producto);
        return productoAssembler.toDTO(actualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }

        productoRepository.deleteById(id);
    }
}
