package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.dtos.ProductoDTO;
import com.backendLevelup.Backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v2/productos") // Actualizado a V2
@Tag(name = "Productos", description = "Operaciones CRUD para el catálogo de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Método auxiliar para HATEOAS (Estilo del profesor)
    private EntityModel<ProductoDTO> buildProductoResource(ProductoDTO producto) {
        EntityModel<ProductoDTO> resource = EntityModel.of(producto);
        // Link a sí mismo
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductoController.class).obtenerProducto(producto.getId())
        ).withSelfRel());
        // Link a la colección completa
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductoController.class).listarProductos()
        ).withRel("todos-los-productos"));
        return resource;
    }

    @Operation(summary = "Crear producto", description = "Agrega un nuevo producto al catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<ProductoDTO>> crearProducto(@RequestBody ProductoDTO dto) {
        ProductoDTO creado = productoService.crearProducto(dto);
        return new ResponseEntity<>(buildProductoResource(creado), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener producto", description = "Busca un producto por su ID único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProducto(@PathVariable Long id) {
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(buildProductoResource(producto));
    }

    @Operation(summary = "Listar productos", description = "Devuelve todos los productos disponibles")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listarProductos() {
        List<ProductoDTO> productos = productoService.listarProductos();

        // Convertir cada DTO a un EntityModel con links
        List<EntityModel<ProductoDTO>> productosResources = productos.stream()
                .map(this::buildProductoResource)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ProductoDTO>> collection = CollectionModel.of(productosResources);

        // Link a la colección misma
        collection.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductoController.class).listarProductos()
        ).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Actualizar producto", description = "Modifica los datos de un producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        ProductoDTO actualizado = productoService.actualizarProducto(id, dto);
        return ResponseEntity.ok(buildProductoResource(actualizado));
    }

    @Operation(summary = "Eliminar producto", description = "Remueve un producto del catálogo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}